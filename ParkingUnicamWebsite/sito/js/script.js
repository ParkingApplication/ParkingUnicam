// create the module
var indexApp = angular.module('parkingApp', ['ngRoute', 'ngStorage']);
var ipserver = "172.16.0.212:5044";
var protocol = "http";

//  Aggiunta della variabile e funzione globali per nascondere/mostrare il menù
indexApp.run(function ($rootScope) {
    $rootScope.logIn = false;

    $rootScope.hideMenu = function (value) {
        $rootScope.logIn = value;
    };
});

// configure our routes
indexApp.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: './home.html',
        controller: 'homeController'
    })
        .when('/login', {
            templateUrl: './login.html',
            controller: 'gestisciLogin'
        })
        .when('/singup', {
            templateUrl: './singup.html',
            controller: 'gestisciSingup'
        })
        .when('/logout', {
            templateUrl: './out.html',
            controller: 'gestisciLogout'
        })
        .when('/parcheggi', {
            templateUrl: './parcheggi.html',
            controller: 'gestisciParcheggi'
        }).when('/mainAdmin', {
            templateUrl: './mainAdmin.html',
            controller: 'gestisciAdmin'
        })
        .when('/utenti', {
            templateUrl: './utenti.html',
            controller: 'gestisciUtenti'
        })
        .when('/prenotazioni', {
            templateUrl: './prenotazioni.html',
            controller: 'gestisciPrenotazioni'
        });
});

//  variabile contenente il token
var curToken = { value: "", enable: false };

var menuSet = function (val) {
    if (val == 0) {
        e1 = document.getElementById("linkHome");   // 0
        e1.setAttribute("class", "active");

        e2 = document.getElementById("linkParcheggi");  // 1
        e2.setAttribute("class", "");

        e3 = document.getElementById("linkMain");   // 2
        e3.setAttribute("class", "");

        e4 = document.getElementById("linkUtenti"); // 3
        e4.setAttribute("class", "");

        e5 = document.getElementById("linkPrenotazioni");   // 4
        e5.setAttribute("class", "");
        $("#navSearch").hide();
        $("#cu1").hide();
        $("#cu2").hide();
        $("#cu3").hide();
    }
    else
        if (val == 1) {
            e1 = document.getElementById("linkHome");   // 0
            e1.setAttribute("class", "");

            e2 = document.getElementById("linkParcheggi");  // 1
            e2.setAttribute("class", "active");

            e3 = document.getElementById("linkMain");   // 2
            e3.setAttribute("class", "");

            e4 = document.getElementById("linkUtenti"); // 3
            e4.setAttribute("class", "");

            e5 = document.getElementById("linkPrenotazioni");   // 4
            e5.setAttribute("class", "");
            $("#navSearch").hide();
            $("#cu1").hide();
            $("#cu2").hide();
            $("#cu3").hide();
        }
        else
            if (val == 2) {
                e1 = document.getElementById("linkHome");   // 0
                e1.setAttribute("class", "");

                e2 = document.getElementById("linkParcheggi");  // 1
                e2.setAttribute("class", "");

                e3 = document.getElementById("linkMain");   // 2
                e3.setAttribute("class", "active");

                e4 = document.getElementById("linkUtenti"); // 3
                e4.setAttribute("class", "");

                e5 = document.getElementById("linkPrenotazioni");   // 4
                e5.setAttribute("class", "");
                $("#navSearch").hide();
                $("#cu1").hide();
                $("#cu2").hide();
                $("#cu3").hide();
            }
            else {
                if (val == 3) {
                    e1 = document.getElementById("linkHome");   // 0
                    e1.setAttribute("class", "");

                    e2 = document.getElementById("linkParcheggi");  // 1
                    e2.setAttribute("class", "");

                    e3 = document.getElementById("linkMain");   // 2
                    e3.setAttribute("class", "");

                    e4 = document.getElementById("linkUtenti"); // 3
                    e4.setAttribute("class", "active");

                    e5 = document.getElementById("linkPrenotazioni");   // 4
                    e5.setAttribute("class", "");
                    $("#navSearch").show();
                    $("#cu1").show();
                    $("#cu2").show();
                    $("#cu3").show();;
                }
                else {
                    if (val == 4) {
                        e1 = document.getElementById("linkHome");   // 0
                        e1.setAttribute("class", "");

                        e2 = document.getElementById("linkParcheggi");  // 1
                        e2.setAttribute("class", "");

                        e3 = document.getElementById("linkMain");   // 2
                        e3.setAttribute("class", "");

                        e4 = document.getElementById("linkUtenti"); // 3
                        e4.setAttribute("class", "");

                        e5 = document.getElementById("linkPrenotazioni");   // 4
                        e5.setAttribute("class", "active");
                        $("#navSearch").show();
                        $("#cu1").hide();
                        $("#cu2").hide();
                        $("#cu3").hide();
                    }
                    else {
                        e1 = document.getElementById("linkHome");   // 0
                        e1.setAttribute("class", "");

                        e2 = document.getElementById("linkParcheggi");  // 1
                        e2.setAttribute("class", "");

                        e3 = document.getElementById("linkMain");   // 2
                        e3.setAttribute("class", "");

                        e4 = document.getElementById("linkUtenti"); // 3
                        e4.setAttribute("class", "");

                        e5 = document.getElementById("linkPrenotazioni");   // 4
                        e5.setAttribute("class", "");
                        $("#navSearch").hide();
                        $("#cu1").hide();
                        $("#cu2").hide();
                        $("#cu3").hide();
                    }
                }
            }

};

// create the controller and inject Angular's $scope
indexApp.controller('homeController', function ($scope, $localStorage, $location) {
    if ($localStorage.XToken) {
        curToken = $localStorage.XToken;
        $scope.hideMenu(true);
        menuSet(2);
        $location.path('/mainAdmin');
    }

    menuSet(0);
});

indexApp.controller('gestisciParcheggi', function ($scope, $http, $localStorage, $location) {
    if ($localStorage.XToken) {
        curToken = $localStorage.XToken;
        $scope.hideMenu(true);
        menuSet(2);
        $location.path('/mainAdmin');
    }

    menuSet(1);

    $http({
        method: "POST",
        url: protocol + "://" + ipserver + "/getAllParcheggi",
        headers: { 'Content-Type': 'application/json' }
    }).then(function (response) {
        if (response.status == 200)
            if (response.data.parcheggi)
                $scope.Parcheggi = response.data.parcheggi;
            else
                alert("Errore sconosciuto!");
        else
            alert("Errore sconosciuto.");

    }, function (response) {
        if (response.data.error)
            alert(response.data.error.info);
        else
            alert("Errore sconosciuto.");
    });
});

indexApp.controller('gestisciUtenti', function ($scope, $http, $localStorage, $location, $window) {
    if ($localStorage.XToken) {
        curToken = $localStorage.XToken;
        $scope.hideMenu(true);
    }

    $scope.page = 1;
    menuSet(3);

    $http({
        method: "POST",
        url: protocol + "://" + ipserver + "/getAllAutisti",
        headers: { 'Content-Type': 'application/json' },
        data: { 'token': curToken.value }
    }).then(function (response) {
        if (response.status == 200)
            if (response.data.autisti) {
                $scope.AllAutisti = response.data.autisti;
                visualizza($scope.page, $scope.AllAutisti);
            }
            else
                alert("Errore sconosciuto!");
    }, function (response) {
        if (response.data.error)
            alert(response.data.error.info);
        else
            alert("Errore sconosciuto.");
    });

    $scope.ModificaUtente = function (index, value) {
        // Nascondo/Mostro i tasti relativi alla funzionalità richiesta
        if (value) {
            $("#username" + index).removeAttr("disabled");
            $("#email" + index).removeAttr("disabled");
            $("#nome" + index).removeAttr("disabled");
            $("#cognome" + index).removeAttr("disabled");
            $("#dataDiNascita" + index).removeAttr("disabled");
            $("#telefono" + index).removeAttr("disabled");
            $("#saldo" + index).removeAttr("disabled");
            $("#numero_carta" + index).removeAttr("disabled");
            $("#pin" + index).removeAttr("disabled");
            $("#dataDiScadenza" + index).removeAttr("disabled");

            $("#modifica" + index).hide();
            $("#elimina" + index).hide();
            $("#annulla" + index).show();
            $("#salva" + index).show();
        }
        else {
            $("#username" + index).attr("disabled", "disabled");
            $("#email" + index).attr("disabled", "disabled");
            $("#nome" + index).attr("disabled", "disabled");
            $("#cognome" + index).attr("disabled", "disabled");
            $("#dataDiNascita" + index).attr("disabled", "disabled");
            $("#telefono" + index).attr("disabled", "disabled");
            $("#saldo" + index).attr("disabled", "disabled");
            $("#numero_carta" + index).attr("disabled", "disabled");
            $("#pin" + index).attr("disabled", "disabled");
            $("#dataDiScadenza" + index).attr("disabled", "disabled");

            $("#modifica" + index).show();
            $("#elimina" + index).show();
            $("#annulla" + index).hide();
            $("#salva" + index).hide();

            //  Ripristino i valori originali
            $("#username" + index).val($scope.Autisti[index].username);
            $("#email" + index).val($scope.Autisti[index].email);
            $("#nome" + index).val($scope.Autisti[index].nome);
            $("#cognome" + index).val($scope.Autisti[index].cognome);
            $("#dataDiNascita" + index).val($scope.Autisti[index].dataDiNascita);
            $("#telefono" + index).val($scope.Autisti[index].telefono);
            $("#saldo" + index).val(parseFloat($scope.Autisti[index].saldo).toFixed(2));
            $("#numero_carta" + index).val($scope.Autisti[index].carta_di_credito.numero_carta);
            $("#pin" + index).val($scope.Autisti[index].carta_di_credito.pin);
            $("#dataDiScadenza" + index).val($scope.Autisti[index].carta_di_credito.dataDiScadenza);
        }
    };

    $scope.SalvaUtente = function (index) {
        var parametri = {
            token: curToken.value,
            autista: {
                id: $scope.Autisti[index].id,
                username: $("#username" + index).val() || $scope.Autisti[index].username,
                password: $scope.Autisti[index].password,
                email: $("#email" + index).val() || $scope.Autisti[index].email,
                nome: $("#nome" + index).val() || $scope.Autisti[index].nome,
                cognome: $("#cognome" + index).val() || $scope.Autisti[index].cognome,
                dataDiNascita: $("#dataDiNascita" + index).val(),
                telefono: $("#telefono" + index).val(),
                saldo: $("#saldo" + index).val(),
                carta_di_credito: {
                    numero_carta: $("#numero_carta" + index).val(),
                    pin: $("#pin" + index).val(),
                    dataDiScadenza: $("#dataDiScadenza" + index).val()
                }
            }
        };

        $http({
            method: "PATCH",
            url: protocol + "://" + ipserver + "/cambiaCredenziali",
            headers: { 'Content-Type': 'application/json' },
            data: parametri
        }).then(function (response) {
            if (response.status == 200)
                if (response.data.successful) {
                    alert(response.data.successful.info);
                    //  Setto i nuovi valori
                    $scope.Autisti[index] = parametri.autista;
                    $scope.ModificaUtente(index, false);
                }
                else
                    alert("Errore sconosciuto!");
            else
                alert("Errore sconosciuto!");
        }, function (response) {
            $scope.ModificaUtente(index, false);
            if (response.data.error)
                alert(response.data.error.info);
            else
                alert("Errore sconosciuto.");
        });
    };

    $scope.EliminaUtente = function (index) {
        if (confirm("Sicuro di voler eliminare questo utente ?")) {
            var parametri = {
                token: curToken.value,
                id: $scope.Autisti[index].id
            };

            $http({
                method: "DELETE",
                url: protocol + "://" + ipserver + "/deleteAutista",
                headers: { 'Content-Type': 'application/json' },
                data: parametri
            }).then(function (response) {
                if (response.status == 200) {
                    if (response.data.successful) {
                        for (var i = ((($scope.page - 1) * 10) + index); i < $scope.AllAutisti.length - 1; i++)
                            $scope.AllAutisti[i] = $scope.AllAutisti[i + 1];

                        $scope.AllAutisti[$scope.AllAutisti.length - 1] = null;
                        $scope.AllAutisti.length = $scope.AllAutisti.length - 1;
                        alert(response.data.successful.info);
                        visualizza($scope.page, $scope.AllAutisti);
                    }
                    else
                        alert("Errore sconosciuto!");
                }
                else
                    alert("Errore sconosciuto!");
            }, function (response) {
                if (response.data.error)
                    alert(response.data.error.info);
                else
                    alert("Errore sconosciuto.");
            });
        };
    };

    $("#cercaPerNome").click(function () {
        $("#cercaPer").text("Cerca per nome");
        $scope.cercaPer = 1;
    });
    $("#cercaPerCognome").click(function () {
        $("#cercaPer").text("Cerca per cognome");
        $scope.cercaPer = 2;
    });
    $("#cercaPerID").click(function () {
        $("#cercaPer").text("Cerca per id");
        $scope.cercaPer = 3;
    });

    var visualizza = function (pagina, users) {
        var user_for_page = 10;
        var start = user_for_page * (pagina - 1);
        var end = user_for_page * pagina;

        if (end > users.length)
            end = users.length;

        var app = [];
        var j = 0;

        for (var i = start; i < end; i++) {
            app[j] = users[i];
            j++;
        }

        $scope.Autisti = app;
    };

    $scope.Next = function () {
        var extra = 0;

        if (($scope.AllAutisti.length % 10) > 0)
            extra = 1;

        if ($scope.page + 1 <= ($scope.AllAutisti.length / 10) + extra) {
            $scope.page++;
            visualizza($scope.page, $scope.AllAutisti);
            $window.scrollTo(0, 0);
        }
    };

    $scope.Previous = function () {
        if ($scope.page > 1) {
            $scope.page--;
            visualizza($scope.page, $scope.AllAutisti);
            $window.scrollTo(0, 0);
        }
    };
});

indexApp.controller('gestisciPrenotazioni', function ($scope, $http, $localStorage, $location) {
    if ($localStorage.XToken) {
        curToken = $localStorage.XToken;
        $scope.hideMenu(true);
    }

    menuSet(4);
});

indexApp.controller('gestisciAdmin', function ($scope, $http, $localStorage) {
    if ($localStorage.XToken) {
        curToken = $localStorage.XToken;
        $scope.hideMenu(true);
    }

    menuSet(2);

});

indexApp.controller('gestisciLogout', function ($scope, $location, $localStorage) {
    $scope.hideMenu(false);
    curToken = { value: "", enable: false };
    $localStorage.XToken = null;
    $location.path('/');
});

/**
 * Username is the e-mail of the persone who want to access in it's personal profile
 */
indexApp.controller('gestisciLogin', function ($scope, $http, $location, $localStorage) {
    if ($localStorage.XToken) {
        curToken = $localStorage.XToken;
        $scope.hideMenu(true);
        menuSet(2);
        $location.path('/mainAdmin');
    }

    menuSet();


    //   Autenticazione via token (se si è precedentementi loggati)
    if (curToken.enable == true) {
        $http({
            method: "POST",
            url: protocol + "://" + ipserver + "/mainAdmin",
            headers: { 'Content-Type': 'application/json' },
            data: { 'token': curToken.value }
        }).then(function (response) {
            if (response.status == 200) {
                if (response.data.success) {
                    alert(response.data.message);
                    $location.path('/home');
                }
                else
                    alert("Errore sconosciuto!");
            }
            else
                alert("Errore sconosciuto!");
        }, function (response) {
            if (response.data.error)
                alert(response.data.error.info);
            else
                alert("Errore sconosciuto.");
        });
    }

    // funzione per l'invio dei dati di login a node
    $scope.login = function () {
        if ($scope.username == undefined || $scope.password == undefined)
            return;

        var parametri = {
            username: $scope.username,
            password: $scope.password, //CryptoJS.SHA1($scope.password).toString() <-----------------------------------------
            admin: true
        };

        $http({
            method: "POST",
            url: protocol + "://" + ipserver + "/login",
            headers: { 'Content-Type': 'application/json' },
            data: parametri
        }).then(function (response) {
            if (response.status == 200) {
                if (!response.data.admin || !response.data.token)
                    alert("Si è verificato un errore nella richiesta di autenticazione!");
                else {
                    curToken.value = response.data.token;
                    curToken.enable = true;
                    $localStorage.admin = response.data.admin;
                    $scope.hideMenu(true);
                    $localStorage.XToken = curToken;
                    $location.path('/mainAdmin');
                }
            }
        }, function (response) {
            if (response.data.error)
                alert(response.data.error.info);
            else
                alert("Errore sconosciuto!")
        });
    }
});

indexApp.controller("gestisciSingup", function ($scope, $http, $location, $localStorage) {
    // Tolgo l'ora dal datetime per la data di nascita dell' utenet
    $('#datanascita').datetimepicker({
        format: 'YYYY/MM/DD'
    });

    /*$scope.datanascita.datetimepicker({
        format: 'DD/MM/YYYY'
    });*/

    menuSet();

    $scope.message = "Registrati";

    $scope.registra = function () {

        $scope.datanascita = $('#nascita').val(); // Non riesco ad ottenerlo diversamente

        var parametri = {
            autista: {
                username: $scope.username,
                password: $scope.password, // CryptoJS.SHA1($scope.password).toString() <--------------------------------------------LEGGERE
                email: $scope.email,
                nome: $scope.nome,
                cognome: $scope.cognome,
                dataDiNascita: $scope.datanascita,
                telefono: $scope.telefono
            }
        };

        $http({
            method: "POST",
            url: protocol + "://" + ipserver + "/signup",
            headers: { 'Content-Type': 'application/json' },
            data: parametri
        }).then(function (response) {
            if (response.status == 200) {
                if (response.data.successful) {
                    alert(response.data.successful.info);
                    $location.path('/');
                }
                else
                    alert("Errore sconosciuto!");
            }
            else
                alert("Errore sconosciuto!");
        }, function (response) {
            if (response.data.error)
                alert(response.data.error.info);
            else
                alert("Errore sconosciuto!");
        });
    };
});