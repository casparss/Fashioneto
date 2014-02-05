define(function(require){
	
	var

	//Deps

	$            = require("jquery"),
	Backbone     = require("backbone"),
	Handlebars   = require("handlebars"),	
	Helper       = require('helper'),
	
	//Base page view
	
	BasePageView = require("views/basepageview"),
	
	//Tab views
	
	Wall         = require("views/wall/wall.js"),
	Photos       = require("views/photos/photos.js"),
	Items        = require("views/items/items.js"),
	
	//Main profile template
	
	template     = require("text!templates/pages/profile.hbr");
	

	return BasePageView.extend({
		initialize: function(options){
			this.options = options || {};
			this.setEvents();
			this.instantiateTabs();
		},

		events:{
			"click .nav-tabs a" : "clickState"
		},

		setEvents: function(){
			App.vent.on("page:profile", this.render, this);	
		},

		template: Handlebars.compile( template ),

		render: function( tab ){
			this.$el.html( this.template() );
			this.tabs[0]
			.setElement( "#tabContainer" )
			.render();

			//Open selected tab
		},

		clickState: function( ev ){
			Helper.clickState( ".nav-tabs a", ev );
		},

		instantiateTabs: function(){
			this.tabs = [
				new Wall(),
				new Photos(),
				new Items()
			];

			for( var i = 0; this.tabs.length < i;  i++ ){
				this.tabs[i].setElement( "#tabContainer" );
			};
		}

	});

});