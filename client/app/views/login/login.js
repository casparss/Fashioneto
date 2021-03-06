define(function(require){

	var

	$                 = require( "jquery" ),
	Handlebars        = require( "handlebars" ),
	Cookie            = require( "jquery.cookie" ),

	User              = require( "models/user" ),
	MasterBaseView    = require( "views/masterbaseview" ),
	mainLoginTemplate = require( "text!templates/login/mainlogin.hbr" ),
	loginFormTemplate = require( "text!templates/login/loginform.hbr" );


	return MasterBaseView.extend({

		el: document.body,

		templates: {
			loginForm: Handlebars.compile( loginFormTemplate ),
			mainLogin: Handlebars.compile( mainLoginTemplate )
		},

		events: {
			"click .loginbtn" : "login"
		},

		nodes: {
			username: ".username",
			password: ".password",
			loginContainer: ".loginform"
		},

		init: function(){
			if( this.checkSessionCookie() ){
				this.getUser();
			} else {
				this.renderMainLogin();
			}
		},

		renderMainLogin: function(){
			this.$el.css( { opacity:0, "margin-left":"20px" } );
			this.$el
			.html( this.templates.mainLogin() )
			.addClass( "login" )
			.find( this.nodes.loginContainer )
			.html( this.templates.loginForm() );
			this.$el.animate( { "margin-left":"0", opacity:1 }, 1000 );
		},


		//Login actions


		login: function( ev ){
			ev.preventDefault();
			var form, loginCredentials;

			form = $( ev.target ).parents( "form" );

			loginCredentials = {
				username: form.find( this.nodes.username ).val(),
				password: form.find( this.nodes.password ).val()
			}

			$.ajax({
				type: "POST",
				context: this,
				url: App.api.get( 'login' ),
				data: loginCredentials,
				success: this.success,
				error: function( jqXHR, textStatus, errorThrown ){
					if( jqXHR.status === 401 ){
						alert( "Incorrect login credentials. Please try again!" );
					} else{
						alert( "login method: " + jqXHR.status + ": " + errorThrown  );
					}
				}

			});

		},

		success: function( data, textStatus, jqXHR ){
			$.cookie( "fashioneto", data.token, {
				expires : 10
			});
			$.ajaxSetup({
				headers: { 'X-Auth-Token': data.token }
			});

			//Gonna get Felipe to refactor so details and id are returned only
			data.user.details.id = data.user.id;
			App.user = new User( data.user.details );
			this.proceed( data.user );
		},
		
		
		logout: function(){
			$.removeCookie("fashioneto");
			$.ajaxSetup({
				headers: { 'X-Auth-Token': "" }
			});
			App.data = {
		        myprofile:{},
		        guestprofile:{},
		        items:{},
		        people:{}
		    }
			this.renderMainLogin();
		},

		checkSessionCookie: function(){
			var sessionToken = $.cookie("fashioneto");
			if( sessionToken ){
				$.ajaxSetup({
				    headers: { 'X-Auth-Token': sessionToken }
				});
				return true;
			} else {
				return false;
			}
		},

		getUser: function(){
			$.ajax({
				type: "GET",
				context: this,
				url: App.api.get( 'user' ),
				dataType: "JSON",

				success: function( data, textStatus, jqXHR ){
					App.user = new User( data.details );
					this.proceed();
				},

				error: function( jqXHR, textStatus, errorThrown ){
					if( jqXHR.status === 401 || jqXHR.status === 404 ){
						this.logout();
					} else {
						console.log("getUser method: " + jqXHR.status + ": " + errorThrown);
					}

				}

			});
		},

		proceed: function( data ){
			App.vent.trigger( "login:load", data );
			App.vent.on( "login:sessionExpired", this.modalLogin, this );
			App.vent.on( "login:logout", this.logout, this );
			this.options.success.call( this.options.context );
		}

	});

});


