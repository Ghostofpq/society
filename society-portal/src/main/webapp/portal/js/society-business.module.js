'use strict';

var defaults = {
    size: 20,
    page: 0
}

angular.module('society-business', [])
    .value('baseUrl', '0.1')
    .provider("$societyBusiness", function(){
        var baseUrl = "/api/";
     	var headers = {
     	    "Content-Type" : "application/json"
     	};

        return {
            baseUrl: function(v){
            	baseUrl=v;
            },
            headers: function(h){
            	headers=h;
            },
            // return service
            $get: function($http){
                return {
                    baseUrl: function(){
                        return baseUrl;
                    },

                    getUser:function(id){
                       var url = baseUrl + "users/" + id;
                       return $http.get(url);
                    },

                    updateUserLogin: function(id,login){
                        var url = baseUrl + "users/" + id + "/login";
                        return $http.put(url, login);
                    }
                }
            }
        }
    });