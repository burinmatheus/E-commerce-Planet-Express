//FAVORITOS
function removerFavoritos(elem) {
    if (localStorage.getItem('Authorization') != null) {
        let funcao = 'removerFavoritos';
        let caminho = 'EJBPlanetExpress/Favoritos';
        let idproduto = elem.parentNode.id;

        let corpo = `funcao=${funcao}&caminho=${caminho}&id_produto=${idproduto}`;
        requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);

        document.getElementsByClassName('favoritos')[0].click();
    } else {
        alert('Você deve logar para prosseguir com esta ação!!!');
        openLogin(1);
    }
}

function adicionarFavoritos(elem) {
    if (localStorage.getItem('Authorization') != null) {
        let funcao = 'adicionarFavoritos';
        let caminho = 'EJBPlanetExpress/Favoritos';
        let idproduto = elem.parentNode.id;

        let corpo = `funcao=${funcao}&caminho=${caminho}&id_produto=${idproduto}`;
        requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);
    } else {
        alert('Você deve logar para prosseguir com esta ação!!!');
        openLogin(1);
    }
}

//CARRINHO DE COMPRAS
function atualizaqtde(elem) {

    let funcao = ' ';
    let caminho = 'EJBPlanetExpress/Pedidos';
    let idproduto = elem.parentNode.parentNode.classList[1];

    if (elem.classList[2] == 'aumentar') {
        funcao = 'addProduto';

    } else if (elem.classList[2] == 'diminuir') {
        if (elem.parentNode.childNodes[1].value == 1) {
            alert('Se você deseja remover o produto, clique no "X" respectivo ao mesmo!!!');
            return false;

        } else {
            funcao = 'diminuiQTDE';

        }
    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&id_produto=${idproduto}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, atualizavalor, alerterror);
}

function atualizavalor(resp) {
    if (JSON.parse(resp).alerta) {
        let funcao = 'listarProdutosPedidoA';
        let caminho = 'EJBPlanetExpress/Pedidos';

        let corpo = `funcao=${funcao}&caminho=${caminho}`;
        requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, atualizavalor, alerterror);

    } else {

        let respostas = JSON.parse(resp);
        respostas = respostas.produtos;

        let valortotal = 0;

        respostas.forEach(resposta => {
            let elemento = document.getElementsByClassName('produtolistado ' + resposta.id + '')[0];

            elemento.childNodes[2].childNodes[1].value = resposta.qtde;

            if (resposta.desconto != 0) {
                elemento.childNodes[3].childNodes[0].innerText = ((resposta.valor - (resposta.valor * (resposta.desconto / 100))) * resposta.qtde).toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

                valortotal = parseFloat(valortotal) + parseFloat((((resposta.valor - (resposta.valor * (resposta.desconto / 100))) * resposta.qtde).toFixed(2)));
            } else {
                elemento.childNodes[3].childNodes[0].innerText = (resposta.valor * resposta.qtde).toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

                valortotal = parseFloat(valortotal) + parseFloat((resposta.valor * resposta.qtde));
            }

        });

        document.getElementById('h2total').innerText = 'TOTAL R$: ' + valortotal.toLocaleString('pt-br', { minimumFractionDigits: 2 });
    }
}

function removeProdutoCarrinho(elem) {
    let funcao = 'removerProduto';
    let caminho = 'EJBPlanetExpress/Pedidos';
    let idproduto = elem.parentNode.parentNode.classList[1];

    let corpo = `funcao=${funcao}&caminho=${caminho}&id_produto=${idproduto}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);

    listarCarrinho();

}

function addCarrinho(elem) {
    if (localStorage.getItem('Authorization') != null) {
        let funcao = 'addProduto';
        let caminho = 'EJBPlanetExpress/Pedidos';
        let idproduto = elem.parentNode.id;

        let corpo = `funcao=${funcao}&caminho=${caminho}&id_produto=${idproduto}`;
        requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);
    } else {
        alert('Você deve logar para prosseguir com esta ação!!!');
        openLogin(1);
    }
}

function fecharPedido() {
    let funcao = 'finalizaPedido';
    let caminho = 'EJBPlanetExpress/Pedidos';

    let corpo = `funcao=${funcao}&caminho=${caminho}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);

    document.getElementsByClassName('favoritos')[0].click();
}

//USER
function buscaUser(elem) {
    let funcao = 'buscarUser';
    let caminho = 'EJBPlanetExpress/User';
    let logininfo = elem.value;

    let corpo = `funcao=${funcao}&caminho=${caminho}&logininfo=${logininfo}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, function(resp) {

        let resposta = JSON.parse(resp);

        localStorage.setItem('UserId', resposta.id);
        localStorage.setItem('UserImg', resposta.img);
        localStorage.setItem('UserNome', resposta.nome);

        document.getElementById('user-ft').childNodes[1].src = '/ServletPlanetExpress/retornaimagem?tipo=users&cod=' + resposta.img;

    }, function(resp) {
        alerterror(resp);
        document.querySelector('input.loginadd').value = ' ';
    });

}

function continuarRegistro() {
    if (document.querySelector('input#senha').value != '' & document.querySelector('input#senha').value == document.querySelector('input#repsenha').value) {
        openRegistro(0);
        openRegistro(2);
    } else {
        alert('Preencha os campos de senha corretamente!!!');
    }
}

function registrar() {
    let funcao = 'criarUsuario';
    let caminho = 'EJBPlanetExpress/User';

    let nome;
    let sobrenome;
    let img;
    let senha;
    let email;
    let telefone;
    let cpf;
    let rg;
    let estado_id;
    let cidade;
    let cep;
    let bairro;
    let rua;
    let numero;

    if (document.querySelector('input#nome').value != '') {
        nome = document.querySelector('input#nome').value;

        if (document.querySelector('input#sobrenome').value != '') {
            sobrenome = document.querySelector('input#sobrenome').value;

            if (document.querySelector('.userRegImg').childNodes[1].className != '') {
                img = document.querySelector('.userRegImg').childNodes[1].className;

                if (document.querySelector('input#email').value != '') {
                    email = document.querySelector('input#email').value;

                    if (document.querySelector('input#telefone').value != '') {
                        telefone = document.querySelector('input#telefone').value;

                        if (document.querySelector('input#senha').value != '') {
                            senha = document.querySelector('input#senha').value;

                            if (document.querySelector('input#cpf').value != '') {
                                cpf = document.querySelector('input#cpf').value;

                                if (document.querySelector('input#rg').value != '') {
                                    rg = document.querySelector('input#rg').value;

                                    if (document.querySelector('select#estado').value != '') {
                                        estado_id = document.querySelector('select#estado').value;

                                        if (document.querySelector('input#cidade').value != '') {
                                            cidade = document.querySelector('input#cidade').value;

                                            if (document.querySelector('input#cep').value != '') {
                                                cep = document.querySelector('input#cep').value;

                                                if (document.querySelector('input#bairro').value != '') {
                                                    bairro = document.querySelector('input#bairro').value;

                                                    if (document.querySelector('input#rua').value != '') {
                                                        rua = document.querySelector('input#rua').value;

                                                        if (document.querySelector('input#numero').value != '') {
                                                            numero = document.querySelector('input#numero').value;

                                                        } else {
                                                            alert('Insira o número da sua casa !!!');
                                                            return false;
                                                        }
                                                    } else {
                                                        alert('Insira o nome da sua rua !!!');
                                                        return false;
                                                    }
                                                } else {
                                                    alert('Insira o seu bairro !!!');
                                                    return false;
                                                }
                                            } else {
                                                alert('Insira o seu CEP !!!');
                                                return false;
                                            }
                                        } else {
                                            alert('Insira o sua cidade !!!');
                                            return false;
                                        }
                                    } else {
                                        alert('Selecione o seu estado !!!');
                                        return false;
                                    }
                                } else {
                                    alert('Insira o seu RG !!!');
                                    return false;
                                }
                            } else {
                                alert('Insira o seu CPF !!!');
                                return false;
                            }
                        } else {
                            alert('Insira o sua Senha !!!');
                            return false;
                        }
                    } else {
                        alert('Insira o seu Telefone !!!');
                        return false;
                    }
                } else {
                    alert('Insira o seu Email !!!');
                    return false;
                }
            } else {
                alert('Imagem não localizada !!!');
                return false;
            }
        } else {
            alert('Insira o seu sobrenome !!!');
            return false;
        }
    } else {
        alert('Insira o seu nome !!!');
        return false;
    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&cpf=${cpf}&nome=${nome}&sobrenome=${sobrenome}&img=${img}&senha=${senha}&email=${email}&telefone=${telefone}&cpf=${cpf}&rg=${rg}&estado=${estado_id}&cidade=${cidade}&cep=${cep}&bairro=${bairro}&rua=${rua}&numero=${numero}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);
    openRegistro(0);
    openLogin(1);
}

function logar(elem) {
    let funcao = 'autenticaUser';
    let caminho = 'EJBPlanetExpress/User';
    let id = localStorage.getItem('UserId');
    let pass = elem.parentNode.childNodes[3].value;

    let corpo = `funcao=${funcao}&caminho=${caminho}&id=${id}&senha=${pass}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, function(resp) {

        let resposta = JSON.parse(resp);
        if (resposta.token != undefined) {
            localStorage.setItem('Authorization', resposta.token);
            openLogin(0);
        } else {
            alert('Informações incorretas!');
            return false;
        }

    }, alerterror);
}

function salvarEndereco() {
    let funcao = 'modificaEndereco';
    let caminho = 'EJBPlanetExpress/User';

    let estado_id;
    let cidade;
    let cep;
    let bairro;
    let rua;
    let numero;

    let cpf = document.getElementById('usercpf').value;

    if (document.getElementById('userestado').value != '') {
        estado_id = document.getElementById('userestado').value;

        if (document.getElementById('usercidade').value != '') {
            cidade = document.getElementById('usercidade').value;

            if (document.getElementById('usercep').value != '') {
                cep = document.getElementById('usercep').value;

                if (document.getElementById('userbairro').value != '') {
                    bairro = document.getElementById('userbairro').value;

                    if (document.getElementById('userrua').value != '') {
                        rua = document.getElementById('userrua').value;

                        if (document.getElementById('usernumero').value != '') {
                            numero = document.getElementById('usernumero').value;

                        } else {
                            alert('Insira o número da sua casa !!!');
                            return false;
                        }
                    } else {
                        alert('Insira o nome da sua rua !!!');
                        return false;
                    }
                } else {
                    alert('Insira o seu bairro !!!');
                    return false;
                }
            } else {
                alert('Insira o seu CEP !!!');
                return false;
            }
        } else {
            alert('Insira o sua cidade !!!');
            return false;
        }
    } else {
        alert('Selecione o seu estado !!!');
        return false;
    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&cpf=${cpf}&estado=${estado_id}&cidade=${cidade}&cep=${cep}&bairro=${bairro}&rua=${rua}&numero=${numero}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);

    edicaodeendereco(0);
}

function salvarDados() {
    let funcao = 'modificarUsuario';
    let caminho = 'EJBPlanetExpress/User';

    let nome;
    let sobrenome;
    let img;
    let email;
    let telefone;

    let cpf = document.getElementById('usercpf').value;

    if (document.getElementById('usernome').value != '') {
        nome = document.getElementById('usernome').value;

        if (document.getElementById('usersobrenome').value != '') {
            sobrenome = document.getElementById('usersobrenome').value;

            if (document.getElementById('userImg').className != '') {
                img = document.getElementById('userImg').className;

                if (document.getElementById('useremail').value != '') {
                    email = document.getElementById('useremail').value;

                    if (document.getElementById('usertelefone').value != '') {
                        telefone = document.getElementById('usertelefone').value;

                    } else {
                        alert('Insira o seu Telefone !!!');
                        return false;
                    }
                } else {
                    alert('Insira o seu Email !!!');
                    return false;
                }
            } else {
                alert('Imagem não localizada !!!');
                return false;
            }
        } else {
            alert('Insira o seu sobrenome !!!');
            return false;
        }
    } else {
        alert('Insira o seu nome !!!');
        return false;
    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&cpf=${cpf}&nome=${nome}&sobrenome=${sobrenome}&img=${img}&email=${email}&telefone=${telefone}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);

    edicaodecampos(0);
}

function salvaSenha() {
    let funcao = 'modificarSenha';
    let caminho = 'EJBPlanetExpress/User';

    let senha;
    let cpf;

    if (document.getElementById('usersenha').value != '') {
        senha = document.getElementById('usersenha').value;

        if (document.getElementById('usercpf').value != '') {
            cpf = document.getElementById('usercpf').value;

        } else {
            alert('Campo de CPF em branco !!!');
            return false;
        }
    } else {
        alert('Insira uma senha para atualizar.');
        return false;
    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&cpf=${cpf}&senha=${senha}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);
}