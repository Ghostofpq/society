'use strict';

app
    // error 403 => reload
	.config(function($httpProvider){

		// setting interceptor on http errors :
		$httpProvider.interceptors.push(function($q) {
			return {
				'responseError': function(rejection) {
					// => partial for API call failed => reload page.
					if(rejection.status == 403){
						console.log("intercepted HTTP 403 => page reload");
						window.location.reload();
					}
					return $q.reject(rejection);
				}
			};
		});
	})

	// Routes
	.config(function($routeProvider) {
		$routeProvider
			// Dashboard
			// -----------------
			.when('/dashboard', {
				templateUrl: 'partials/dashboard.html',
				controller: "dashboard",
			})
			// Default
			// -----------------
			.otherwise({
				redirectTo: '/dashboard'
			});
	})

	// societyBusiness
	.config(function($societyBusiness) {
		$iaeNetOpProvider.baseUrl("../api/society");
	});



