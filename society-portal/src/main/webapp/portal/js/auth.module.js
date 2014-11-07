'use strict';

angular.module('authentication', [])
        // Version number
		.value('version', '0.1')

		// AuthentificationService
		.service("$auth", function($http, $location) {
		    var user = undefined;
			return {
				// get current loggedin User account info
				update: function() {
				    console.log("update Auth");
                    return $http.get("/info")
                        .success(function(u){
                            user=u;
                        })
                },
				getUser: function() {
					return user;
				},

				// logout
				logout: function() {
					return $http.get("http://localhost:1337/session/logout")
						.success(function(){
							location.reload();
						});
				},
				hasRole: function(role){
                    if(angular.isUndefined(user)) return false;
                    for(var i in user.userRole){
                        if(user.userRole[i] == role) return true;
                    }
                    return false;
                }
			};
		});
