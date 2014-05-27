define(function(require){

	var

	_               = require("_"),

	ItemsCollection = require( "collections/items/items" ),
	MetaCollection  = require( "collections/items/meta" ),

	ItemView        = require( "views/items/item" ),
	MasterBaseView  = require( "views/masterbaseview" );


	return MasterBaseView.extend({

		modelView: ItemView,

		emptyCollectionTemplate: Handlebars.compile( "<h1 style='text-align:center'>No items yet!</h1><p style='text-align:center'>(Come on, you're better than this)</p>" ),

		init: function(){
			this.collection = new ItemsCollection();
			this.metaCollection = new MetaCollection();
			App.vent.on( "profile:dataLoaded", this.update, this );

			window.itemsTab = this;
		},

		update: function( data ){
			var items = data.get( "itemsWrapper" ).collection;
			this.collection.fetchById( { prodid: _.pluck( items, "id" ) } );
			this.metaCollection.reset( items );
		},

		activate: function( el ){
			//Render loading until callback replaces content
			$( el )
			.find( "#tabContainer" )
			.html( '<div class="spinner-wave"><div></div><div></div><div></div><div></div></div>' );

			this.listenToOnce( this.collection, "sync", this.renderItems);
		},

		renderItems: function(){
			this.renderCollection();
			var self = this;
			_.defer( function(){
				self.masonry( '.item' );
			} );
			return this;
		}

	});

});