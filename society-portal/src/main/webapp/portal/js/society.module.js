'use strict';

var defaults = {
    size: 20,
    page: 0
}

var societyModule = angular.module("society", ["authentication"])
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

                    deleteUser:function(id){
                        var url = baseUrl + "users/" + id;
                        return $http.delete(url);
                    },

                    updateUserLogin: function(id,login){
                        var url = baseUrl + "users/" + id + "/login";
                        return $http.put(url, login);
                    },

                    createOrganization: function(name,description,owner){
                        var url = baseUrl + "organizations";
                        var organizationCreationRequest={};
                        organizationCreationRequest.name=name;
                        organizationCreationRequest.description=description;
                        organizationCreationRequest.owner=owner;
                        console.log(organizationCreationRequest)
                        return $http.post(url, organizationCreationRequest);
                    },

                    getOrganizations:function(name,user,page,size){
                        var url = baseUrl + "organizations";
                        var p = params();
                        if(!angular.isUndefined(name)){
                            p.add("name", name);
                        }
                        if(!angular.isUndefined(user)){
                            p.add("user", user);
                        }
                        if(!angular.isUndefined(page) && page>=0){
                            p.add("page", page);
                        }
                        if(!angular.isUndefined(size) && size>=0){
                            p.add("size", size);
                        }
                        return $http.get(url+ p.toString());
                    }
                }
            }
        }
    });