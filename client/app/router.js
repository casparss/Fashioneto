require([
	'backbone',
	"_",
	'jquery',
	'handlebars',
	'views/login/login',
	'views/main',
	'helper',
	'bootstrap',
	'jquery.masonry'
],
function( Backbone, _, $, Handlebars, Login, MainView, Helper, bootstrap, masonry ){


	var Router = Backbone.Router.extend({


		initialize: function(){
			this.login = new Login({
				success: function(){
					this.mainView = new MainView();
				},
				context: this
			});
		},


		//Route definitions hash


		routes:{

			'': "index",
			'profile': 'profile',
			'profile/:tab': 'profile',

			'people': 'people',
			'people/:tab': 'people',

			'items': 'items',
			'items/:tab': 'items',

			'itemmodal': 'itemModal',
			'photomodal': 'photoModal',

			'logout' : 'logout'

		},


		//Route mapped methods


		index:function(){
			this.profile( "wall" );
		},

		feed: function( tab ){
			App.vent.trigger( 'page:feed', tab );
		},
		people: function( tab ){
			App.vent.trigger( 'page:people', tab );
		},
		items: function( tab ){
			App.vent.trigger( 'page:items', tab );
		},
		profile: function( tab ){
			App.vent.trigger( 'page:profile', tab );
		},

		itemModal: function(){
			$('#itemModal').modal();
		},

		photoModal: function(){
			$('#photoModal').modal();
		},

		logout: function(){
			App.vent.trigger( 'login:logout' );
		}


	});


	//Instantiate router and start Backbone history


	var router   = new Router;
	Backbone.history.start();


});






