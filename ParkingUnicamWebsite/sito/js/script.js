// create the module
var indexApp = angular.module('parkingApp', ['ngRoute', 'ngStorage']);
var ipserver = "172.16.0.212:5044";
var protocol = "http";

//  variabile contenente il token
var curToken = { value: "", enable: false };

// variabili per il menu
var searchFor = 1;

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

var menuSet = function (val) {
    if (val == 0) {
        $("#linkHome").addClass("active");
        $("#linkParcheggi").removeClass("active");
        $("#linkMain").removeClass("active");
        $("#linkUtenti").removeClass("active");
        $("#linkPrenotazioni").removeClass("active");

        $("#navSearch").hide();
        $("#cercaPerNome").hide();
        $("#cercaPerCognome").hide();
        $("#cercaPerID").hide();
    }
    else
        if (val == 1) {
            $("#linkHome").removeClass("active");
            $("#linkParcheggi").addClass("active");
            $("#linkMain").removeClass("active");
            $("#linkUtenti").removeClass("active");
            $("#linkPrenotazioni").removeClass("active");

            $("#navSearch").hide();
            $("#cercaPerNome").hide();
            $("#cercaPerCognome").hide();
            $("#cercaPerID").hide();
        }
        else
            if (val == 2) {
                $("#linkHome").removeClass("active");
                $("#linkParcheggi").removeClass("active");
                $("#linkMain").addClass("active");
                $("#linkUtenti").removeClass("active");
                $("#linkPrenotazioni").removeClass("active");

                $("#navSearch").hide();
                $("#cercaPerNome").hide();
                $("#cercaPerCognome").hide();
                $("#cercaPerID").hide();
            }
            else {
                if (val == 3) {
                    $("#linkHome").removeClass("active");
                    $("#linkParcheggi").removeClass("active");
                    $("#linkMain").removeClass("active");
                    $("#linkUtenti").addClass("active");
                    $("#linkPrenotazioni").removeClass("active");

                    $("#navSearch").show();
                    $("#cercaPerNome").show();
                    $("#cercaPerCognome").show();
                    $("#cercaPerID").show();;
                }
                else {
                    if (val == 4) {
                        $("#linkHome").removeClass("active");
                        $("#linkParcheggi").removeClass("active");
                        $("#linkMain").removeClass("active");
                        $("#linkUtenti").removeClass("active");
                        $("#linkPrenotazioni").addClass("active");

                        $("#navSearch").show();
                        $("#cercaPerNome").hide();
                        $("#cercaPerCognome").hide();
                        $("#cercaPerID").hide();
                    }
                    else {
                        $("#linkHome").removeClass("active");
                        $("#linkParcheggi").removeClass("active");
                        $("#linkMain").removeClass("active");
                        $("#linkUtenti").removeClass("active");
                        $("#linkPrenotazioni").removeClass("active");

                        $("#navSearch").hide();
                        $("#cercaPerNome").hide();
                        $("#cercaPerCognome").hide();
                        $("#cercaPerID").hide();
                    }
                }
            }
};

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
    searchFor = 1;
    $scope.AllAutisti = [];
    $scope.Autisti = [];
    $scope.Search = [];
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

    var IndexFromId = function (id) {
        var j;
        for (j = 0; j < $scope.AllAutisti.length; j++)
            if ($scope.AllAutisti[j].id == id)
                return j;
    };

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
            $("#abilitato" + index).removeAttr("disabled");
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
            $("#abilitato" + index).attr("disabled", "disabled");
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
            $("#abilitato" + index).prop('checked', $scope.Autisti[index].abilitato);
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
                abilitato: $("#abilitato" + index).is(':checked'),
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
                    $scope.AllAutisti[IndexFromId(parametri.autista.id)] = parametri.autista;
                    if ($scope.Search.length > 0)
                        $scope.Search[(($scope.page - 1) * 10) + index] = parametri.autista;
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
                        for (var i = IndexFromId($scope.Autisti[index].id); i < $scope.AllAutisti.length - 1; i++)
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

    var Search = function () {
        var input = $("#inputSearch").val();
        var output = [];
        var i = 0;

        if ((input == "") || (input == "undefined")) {
            $scope.page = 1;
            $scope.Search = [];
            visualizza($scope.page, $scope.AllAutisti);
        }
        else
            if (searchFor == 1) {   // Cerca per nome
                $scope.AllAutisti.forEach(user => {
                    if (user.nome.toLowerCase().startsWith(input.toLowerCase())) {
                        output[i] = user;
                        i++;
                    }
                });
                $scope.page = 1;
                $scope.Search = output;
                visualizza($scope.page, output);
            }
            else
                if (searchFor == 2) {   // Cerca per cognome
                    $scope.AllAutisti.forEach(user => {
                        if (user.cognome.toLowerCase().startsWith(input.toLowerCase())) {
                            output[i] = user;
                            i++;
                        }
                    });
                    $scope.page = 1;
                    $scope.Search = output;
                    visualizza($scope.page, output);
                }
                else
                    if (searchFor == 3) {   // ID
                        $scope.AllAutisti.forEach(user => {
                            if (user.id == input) {
                                output[i] = user;
                                i++;
                            }
                        });
                        $scope.page = 1;
                        $scope.Search = output;
                        visualizza($scope.page, output);
                    }
                    else {
                        $scope.Search = [];
                        alert("Prima devi selezionare per cosa cercare.");
                    }


    }

    $("#cercaPerNome").click(function () {
        $("#cercaPer").text("Cerca per nome");
        searchFor = 1;
    });

    $("#cercaPerCognome").click(function () {
        $("#cercaPer").text("Cerca per cognome");
        searchFor = 2;
    });

    $("#cercaPerID").click(function () {
        $("#cercaPer").text("Cerca per id");
        searchFor = 3;
    });

    $("#inputSearch").change(Search);
    $("#btnSearch").click(Search);

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

        var vet = [];

        if ($scope.Search.length > 0)
            vet = $scope.Search;
        else
            vet = $scope.AllAutisti;

        if ((vet.length % 10) > 0)
            extra = 1;

        if ($scope.page + 1 <= (vet.length / 10) + extra) {
            $scope.page++;
            visualizza($scope.page, vet);
            $window.scrollTo(0, 0);
        }
    };

    $scope.Previous = function () {
        var vet = [];

        if ($scope.Search.length > 0)
            vet = $scope.Search;
        else
            vet = $scope.AllAutisti;

        if ($scope.page > 1) {
            $scope.page--;
            visualizza($scope.page, vet);
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

    $http({
        method: "POST",
        url: protocol + "://" + ipserver + "/getParcheggiPerCitta",
        headers: { 'Content-Type': 'application/json' },
        data: {
            'token': curToken.value,
            'citta': "Roma"
        }
    }).then(function (response) {
        if (response.status == 200) {
            if (response.data.parcheggi) {
                alert(response.data.parcheggi[0]);
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
            password: CryptoJS.SHA1($scope.password).toString(),
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
                alert("Errore sconosciuto!");
        });
    }
});

indexApp.controller("gestisciSingup", function ($scope, $http, $location, $localStorage) {
    // Tolgo l'ora dal datetime per la data di nascita dell' utenet
    $('#datanascita').datetimepicker({
        format: 'YYYY/MM/DD'
    });

    menuSet();

    $scope.message = "Registrati";

    $scope.registra = function () {

        $scope.datanascita = $('#nascita').val(); // Non riesco ad ottenerlo diversamente

        var parametri = {
            autista: {
                username: $scope.username,
                password: CryptoJS.SHA1($scope.password).toString(),
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