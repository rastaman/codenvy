(function(window){

    define(["jquery","underscore","backbone"], function($,_,Backbone){

        // implementation based on :
        // https://github.com/codenvy/cloud-ide/blob/8fe1e50cc6434899dfdfd7b2e85c82008a39a880/cloud-ide-war/src/main/webapp/js/select-tenant.js

        var Tenant = Backbone.Model.extend({

            initialize : function(){
                this.set("url",this.__buildTenantUrl());
            },

            __buildTenantUrl : function(){
                return window.location.protocol +
                    "//" + window.location.host + "/ide/" + this.get("name") + location.search.substr(0);
            }

        });

        var Tenants = Backbone.Collection.extend({
            url : "/rest/private/profile/tenants",
            model : Tenant,
            parse : function(response){
                return _.map(response, function(r){
                    return { name : r };
                });
            },
            fetch : function(options){
                var dfd = $.Deferred();
                $.when(Backbone.Collection.prototype.fetch.apply(this,options))
                    .done(_.bind(function(){
                        dfd.resolve(this.models);
                    },this))
                    .fail(_.bind(function(){
                        dfd.reject(this);
                    },this));
                return dfd.promise();
            }
        });

        return {
            getTenants : function(){
                return new Tenants().fetch();
            },

            Tenant : Tenant,
            Tenants : Tenants
        };

    });

}(window));
