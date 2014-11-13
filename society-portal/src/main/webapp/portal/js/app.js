'use strict';

var app;

// Declare app level module
app = angular.module('app', [
	'society',
	'toaster',
    'ngRoute',
	'authentication'
	]);


// **** Services ***

app.filter("bigNb", function () {
	return function (val) {

		var n = parseFloat(val);
		if( isNaN(n) ) return "-";
		if (n > 1000000) return Math.floor(n / 1000000) + "M";
		if (n > 1000) return Math.floor(n / 1000) + "k";
		return n.toFixed(0);

	}

});

app.directive("inputEdit", function(){
    return {
        scope: {
            inputEdit: "=",
            model: "=",
        },
        replace: true,
        template:
            "<span>" +
                "<span class='cursor-hand' ng-click='val=model;editing=!editing' ng-hide='editing' ng-transclude></span>" +
                "<span ng-show='editing'>" +
                    "<input ng-model='val' class='form-control'/>" +
                    "<button class='btn btn-default' ng-click='inputEdit(val);editing=false'><i class='fa fa-check'></i></button>" +
                    "<button class='btn btn-default' ng-click='editing=false'><i class='fa fa-times'></i></button>" +
                "</span>" +
            "</span>",
        transclude: true,
    }
});

/*
 * A directive for editable label > textarea.
 * Ex: <span textarea-edit="edit">My current value</span>
 *
 * params:
 *  - textarea-edit : a function called once value has been edited, the value is passed as first argument
 *  - model : current value of the variable
 */
app.directive("textareaEdit", function(){
    return {
        scope: {
            textareaEdit: "=",
            model: "=",
        },
        replace: true,
        template:
            "<span>" +
                "<span class='cursor-hand' ng-click='val=model;editing=!editing' ng-hide='editing' ng-transclude></span>" +
                "<span ng-show='editing'>" +
                    "<textarea ng-model='val' class='form-control'></textarea>" +
                    "<button class='btn btn-default' ng-click='textareaEdit(val);editing=false'><i class='fa fa-check'></i></button>" +
                    "<button class='btn btn-default' ng-click='editing=false'><i class='fa fa-times'></i></button>" +
                "</span>" +
            "</span>",
        transclude: true,
    }
});

/*
 * A directive for editable label > select.
 * Ex: <span select-edit="edit" options="...">My current value</span>
 *
 * params:
 *  - textarea-edit : a function called once value has been edited, the value is passed as first argument
 *  - model : current value of the variable
 *  - options : like ng-options
 */
app.directive("selectEdit", function(){
    return {
        scope: {
            selectEdit: "=",
            model: "=",
            options: "=",
        },
        replace: true,
        template:
            "<span>" +
                "<span class='cursor-hand' ng-click='val=model;editing=!editing' ng-hide='editing' ng-transclude></span>" +
                "<span ng-show='editing'>" +
                    "<select ng-model='val' ng-options='k as v for (k,v) in options' class='form-control'></select>" +
                    "<button class='btn btn-default' ng-click='selectEdit(val);editing=false'><i class='fa fa-check'></i></button>" +
                    "<button class='btn btn-default' ng-click='editing=false'><i class='fa fa-times'></i></button>" +
                "</span>" +
            "</span>",
        transclude: true,
    }
});

app.filter("default", function(){
    return function(input, d){
        if(angular.isUndefined(input) || input === null){
            return d;
        }
        return input;
    }
});

app.controller("$comfirmModelCtrl", function($scope, $modalInstance, title, content){
    $scope.title = title;
    $scope.content = content;
    $scope.yes = function(){
         $modalInstance.close(true);
    }
    $scope.no = function(){
         $modalInstance.close(false);
    }
});

app.factory("$confirm", function($modal){
    return function(title, content){
        var m = $modal.open({
            controller: "$comfirmModelCtrl",
            templateUrl: "partials/modal.confirm.html",
            resolve: {
                title: function(){ return title; },
                content: function(){ return content; },
            }
        });
        return {
            success: function(cb){
                m.result.then(function(res){
                    if(res){
                        cb();
                    }
                })
            }
        }
    }
})