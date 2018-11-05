controllers.controller('ChooseGameController', [
	'$scope',
	'$rootScope',
	'$http',
	function($scope, $rootScope, $http) {
		var vm = $scope;
        var baseUrl = $rootScope.baseUrl;
		vm.newMatch = {};
		vm.gameOptions = [
			{
				id: 1,
				name: "Akn�k v�letlen szer� elhelyez�se a p�ly�n",
				description: "Bla-bla-bla..."
			},
			{
				id: 2,
				name: "Falak v�letlen szer� elhelyez�se a p�ly�n",
				description: "Bla-bla-bla..."
			},
			{
				id: 3,
				name: "Karakterek v�letlenszer� t�rl�se",
				description: "Bla-bla-bla..."
			}
		];
        
		initController();

        function initController() {
            // get gameTypes
			$http.get(baseUrl + '/gametypes')
			.then(function(result){
                vm.gameTypes = result.data;
			});
		};
		
		vm.createChallange = function() {
			vm.loading = true;
			console.log(vm.newMatch);


			vm.loading = false;
		};
	
}]);
