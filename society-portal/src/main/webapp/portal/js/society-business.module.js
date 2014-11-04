'use strict';

var defaults = {
    size: 20,
    page: 0
}

var societyBusiness = angular.module('society-business', [])
    .value('baseUrl', '0.1')
    .provider("$societyBusiness", function(){
        var baseUrl = "./";
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

                }
            }
        }
    });