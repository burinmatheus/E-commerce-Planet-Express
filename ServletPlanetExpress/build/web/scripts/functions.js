//FAVORITOS
function removerFavoritos(elem) {
    let funcao = 'removerFavoritos';
    let caminho = 'EJBPlanetExpress/Favoritos';
    let idproduto = elem.parentNode.id;

    let corpo = `funcao=${funcao}&caminho=${caminho}&id_produto=${idproduto}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);

    document.getElementsByClassName('favoritos')[0].click();
}

function adicionarFavoritos(elem) {
    let funcao = 'adicionarFavoritos';
    let caminho = 'EJBPlanetExpress/Favoritos';
    let idproduto = elem.parentNode.id;

    let corpo = `funcao=${funcao}&caminho=${caminho}&id_produto=${idproduto}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);
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
    let funcao = 'addProduto';
    let caminho = 'EJBPlanetExpress/Pedidos';
    let idproduto = elem.parentNode.id;

    let corpo = `funcao=${funcao}&caminho=${caminho}&id_produto=${idproduto}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);

}

function fecharPedido() {
    let funcao = 'finalizaPedido';
    let caminho = 'EJBPlanetExpress/Pedidos';

    let corpo = `funcao=${funcao}&caminho=${caminho}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);

}