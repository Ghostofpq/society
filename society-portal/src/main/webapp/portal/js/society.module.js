'use strict';

var defaults = {
    size: 20,
    page: 0
}

var societyModule = angular.module("society", [])
    .provider("$society", function(){
       	var baseUrl = "./";
     	var headers = {
     	    "Content-Type" : "application/json"
     	};

        function params(){
            var l = [];
            return {
            	add: function(k, v){
            		if(!angular.isUndefined(v) && v != null){
            			l.push(k+"="+v);
            		}
            	},
            	toString: function(){
            		if(l.length<1){
            			return "";
            		}
            		return "?" + l.join("&");
            	}
            }
        }

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
                        return baseUrl
                    },
                    getAllUser:function(){
                       var url = baseUrl + "users/all";
                       return $http.get(url);
                    },
                    addUser:function(login,password,email){
                        var user = {};
                        user.login=login;
                        user.password=password;
                        user.email=email;
                        var url = baseUrl + "users";
                        console.log(url);
                        console.log(user);
                        return $http.post(url,user);
                    },

                    getUser:function(id){
                       var url = baseUrl + "users/" + id;
                       return $http.get(url);
                    },

                    getUsers:function(page,size){
                        var url = baseUrl + "users/all";
                        var p = params();
                        if(!angular.isUndefined(page) && page>=0){
                            p.add("page", page);
                        }
                        if(!angular.isUndefined(size) && size>=0){
                            p.add("size", size);
                        }
                        return $http.get(url+ p.toString());
                    },


                    updateUserLogin: function(id,login){
                        var url = baseUrl + "users/" + id + "/login";
                        return $http.put(url, login);
                    }
                }
            }
        }
    });