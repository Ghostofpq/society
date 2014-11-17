'use strict';

angular.module('authentication', [])
        // Version number
		.value('version', '0.1')

		// AuthentificationService
		.service("$auth", function($http, $location) {
		    var userToken = undefined;
			return {
				// get current loggedin User account info
				update: function() {
				    console.log("update Auth");
                    return $http.get("/session/user")
                        .success(function(res){
                            console.log(res);
                            userToken=res;
                        })
                },
				getUser: function() {
					return userToken;
				},
				login: function(username,password){
				    return $http.post("http://localhost:1337/session/login?username="+username+"&password="+password);
				},
				// logout
				logout: function() {
					return $http.get("http://localhost:1337/session/logout")
						.success(function(){
							location.reload();
						});
				},
				hasRole: function(role){
                    if(angular.isUndefined(userToken)) return false;
                    for(var i in userToken.authorities){
                        if(userToken.authorities[i].authority == role) return true;
                    }
                    return false;
                }
			};
		});
