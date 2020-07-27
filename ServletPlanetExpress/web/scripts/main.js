var pg = 1;
var working = false;
var tela = 'listarProdutosTodos';
var id = 0;
var box = 'boxtodosprodutos';

function requisicao(url, corpo, callbackOk, callbackErro) {
    var http = new XMLHttpRequest();
    http.open("POST", url, true);
    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    http.addEventListener('load', function() {
        if (http.status < 400)
            callbackOk(http.response);
        else
            callbackErro(http.response);

    });
    http.addEventListener('error', function() {
        callbackErro(`Erro de Rede!`);
    });
    http.send(corpo);
}

function alertresposta(resp) {
    console.log(JSON.parse(resp).data);
}

function alerterror(resp) {
    alert(`Error: ${JSON.parse(resp).erro}`);
}

//SEGMENTOS

function processaSegmentos(resp) {
    let respostas = JSON.parse(resp).segmentos;

    let barraLateral = document.getElementById("barraLateral");
    barraLateral.innerHTML = ' ';

    respostas.forEach(resposta => {
        let segmentobox = document.createElement('div');
        segmentobox.classList.add('segmentobox');

        let imagem = document.createElement('img');
        imagem.classList.add('imagemSegmento');
        imagem.src = "./img/segmentos/" + resposta.imgS + ".svg"

        let p = document.createElement('p');
        p.classList.add('nomeSegmento');
        p.innerHTML = resposta.nomeS;
        p.setAttribute('id', resposta.idS);
        p.setAttribute('onclick', 'trocatela(this)');

        let icon = document.createElement('i');
        icon.classList.add('fas');
        icon.classList.add('fa-chevron-down');

        let categoriabox = document.createElement('div');
        categoriabox.classList.add('categoriabox');

        let cate = resposta.categorias;
        cate.forEach(cat => {
            let pc = document.createElement('p');
            pc.classList.add(cat.idC);
            pc.innerHTML = cat.nomeC;
            pc.setAttribute('onclick', 'trocatela(this)');


            categoriabox.appendChild(pc);
        });

        segmentobox.appendChild(imagem);
        segmentobox.appendChild(p);
        segmentobox.appendChild(icon);
        segmentobox.appendChild(categoriabox);
        barraLateral.appendChild(segmentobox);

    });
}

function carregaSegmentos() {
    let funcao = 'listaSegCat';
    let caminho = 'EJBPlanetExpress/Tela';

    let corpo = `funcao=${funcao}&caminho=${caminho}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processaSegmentos, alerterror);
}

//IMAGENS
function trocaimg(elem) {
    elem.parentNode.parentNode.children[1].children[0].src = elem.src;
}

//PRODUTOS

function processalistarProduto(resp) {
    let respostas = JSON.parse(resp);
    respostas = respostas.produtos;
    respostaimgs = respostas.imagens;

    let produtodetalhes = document.getElementById('produtodetalhes');
    produtodetalhes.innerHTML = ' ';

    //IMGS
    let boximgs = document.createElement('div');
    boximgs.setAttribute('id', 'boximgs');

    let divminiaturas = document.createElement('div');
    divminiaturas.setAttribute('id', 'divminiaturas');

    let divimg = document.createElement('div');
    divimg.setAttribute('id', 'divimg');

    let imgs = document.createElement('img');
    imgs.src = "/ServletPlanetExpress/retornaimagem?tipo=produtos&cod=" + respostaimgs[0].img;

    respostaimgs.forEach(respostaimg => {
        let imgs = document.createElement('img');
        imgs.src = "/ServletPlanetExpress/retornaimagem?tipo=produtos&cod=" + respostaimg.img;
        imgs.setAttribute('onclick', 'trocaimg(this)');

        divminiaturas.appendChild(imgs);
    });

    boximgs.appendChild(divminiaturas);
    divimg.appendChild(imgs);
    boximgs.appendChild(divimg);
    produtodetalhes.appendChild(boximgs);


}

function processalistarProdutos(resp) {
    let respostas = JSON.parse(resp);
    respostas = respostas.produtos;
    pg = pg + 1;

    let boxprodutos = document.getElementById('' + box + '');

    respostas.forEach(resposta => {
        let produtobox = document.createElement('div');
        produtobox.classList.add('produtobox');
        produtobox.setAttribute('id', resposta.id);

        let imagem = document.createElement('img');
        imagem.classList.add('imagemProduto');
        imagem.src = "/ServletPlanetExpress/retornaimagem?tipo=produtos&cod=" + resposta.img;
        produtobox.appendChild(imagem);

        let h3 = document.createElement('h3');
        h3.classList.add('produtonome');
        h3.innerHTML = resposta.nome;
        h3.setAttribute('onclick', 'trocatela(this)');
        produtobox.appendChild(h3);

        let marca = document.createElement('p');
        marca.classList.add('produtomarca');
        marca.innerHTML = resposta.marca;
        produtobox.appendChild(marca);



        if (resposta.desconto != 0) {
            let valorA = document.createElement('p');
            valorA.classList.add('produtovalorA');
            valorA.innerHTML = "R$: " + resposta.valor;
            produtobox.appendChild(valorA);

            let valorB = document.createElement('p');
            valorB.classList.add('produtovalorB');
            valorB.innerHTML = "Por R$: " + (resposta.valor - (resposta.valor * (resposta.desconto / 100))).toFixed(2);;
            produtobox.appendChild(valorB);

            let descontos = document.createElement('span');
            descontos.classList.add('produtodesconto');
            descontos.innerHTML = resposta.desconto + "% de desconto";

            produtobox.appendChild(descontos);
        } else {
            let valorA = document.createElement('p');
            valorA.classList.add('produtovalor');
            valorA.innerHTML = "R$: " + resposta.valor;
            produtobox.appendChild(valorA);
        }

        let btn = document.createElement('div');
        btn.classList.add('btnaddCarrinho');
        btn.innerHTML = "Adicionar ao Carrinho"
        btn.setAttribute('onclick', 'addCarrinho(this)');

        produtobox.appendChild(btn);
        boxprodutos.appendChild(produtobox);

    });
}


//CONTROLLER

function listarProdutosTodos() {
    let funcao = 'listarProdutosTodos';
    let caminho = 'EJBPlanetExpress/Produtos';
    funE = "listarProdutosTodos";

    if (pg == 1) {
        let boxprodutos = document.getElementById("boxtodosprodutos");

        boxprodutos.innerHTML = '  ';

    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&pg=${pg}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistarProdutos, alerterror);
}

function listarProdutosCategoria() {
    let funcao = 'listarProdutosCategoria';
    let caminho = 'EJBPlanetExpress/Produtos';
    funE = "listarProdutosCategoria";

    if (pg == 1) {
        let boxprodutos = document.getElementById("boxlistagemprodutos");

        boxprodutos.innerHTML = '  ';

    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&pg=${pg}&categoria=${id}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistarProdutos, alerterror);
}

function listarProdutosSegmento() {
    let funcao = 'listarProdutosSegmentos';
    let caminho = 'EJBPlanetExpress/Produtos';
    funE = "listarProdutosSegmentos";

    if (pg == 1) {
        let boxprodutos = document.getElementById("boxlistagemprodutos");

        boxprodutos.innerHTML = '  ';

    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&pg=${pg}&segmento=${id}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistarProdutos, alerterror);
}

function listarProdutosPesquisa() {
    let funcao = 'listarProdutosPesquisa';
    let caminho = 'EJBPlanetExpress/Produtos';
    funE = "listarProdutosPesquisa";

    if (pg == 1) {
        let boxprodutos = document.getElementById("boxlistagemprodutos");

        boxprodutos.innerHTML = '  ';

    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&pg=${pg}&pesquisa=${id}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistarProdutos, alerterror);
}

function listarProduto() {
    let funcao = 'listarProduto';
    let caminho = 'EJBPlanetExpress/Produtos';
    funE = " listarProduto";

    if (pg == 1) {
        let boxprodutos = document.getElementById("produtodetalhes");

        boxprodutos.innerHTML = '  ';

    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&id=${id}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistarProduto, alerterror);
}

//TROCA TELA
function trocatela(elem) {
    let identificacao = elem.classList[0];

    if (identificacao == 'nomeSegmento') {
        trocadisplay('boxlistagemprodutos');
        pg = 1;
        tela = 'listarProdutosSegmentos';
        box = 'boxlistagemprodutos';
        id = elem.id;
        listarProdutosSegmento();

    } else if (identificacao == 'produtonome') {
        trocadisplay('produtodetalhes');
        pg = 1;
        tela = 'listarProduto';
        box = 'produtodetalhes';
        id = elem.parentNode.id;
        listarProduto();

    } else if (identificacao == 'logo') {
        trocadisplay('boxtodosprodutos');
        pg = 1;
        tela = 'listarProdutosTodos';
        box = 'boxtodosprodutos';
        listarProdutosTodos();

    } else if (!isNaN(identificacao)) {
        trocadisplay('boxlistagemprodutos');
        pg = 1;
        tela = 'listarProdutosCategoria';
        box = 'boxlistagemprodutos';
        id = identificacao;
        listarProdutosCategoria();

    } else if (identificacao == 'lupa') {
        trocadisplay('boxlistagemprodutos');
        pg = 1;
        tela = 'listarProdutosPesquisa';
        box = 'boxlistagemprodutos';
        id = document.getElementById('campopesquisa').value;
        listarProdutosPesquisa();

    } else if (identificacao == 'carrinho') {

    }
}

function trocadisplay(a) {
    if (a == 'boxtodosprodutos') {
        document.getElementById('inicial').style.display = 'block';
        $('.carousel').slick({
            slidesToShow: 1,
            slidesToScroll: 1,
            autoplay: true,
            autoplaySpeed: 2000,
            arrows: false,
        });
    } else {
        document.getElementById('inicial').style.display = 'none';
        $('.carousel').slick('unslick');
    }

    if (a == 'boxlistagemprodutos') {
        document.getElementById('produtoslistagem').style.display = 'block';
    } else {
        document.getElementById('produtoslistagem').style.display = 'none';
    }

    if (a == 'produtodetalhes') {
        document.getElementById('produtodetalhes').style.display = 'block';
    } else {
        document.getElementById('produtodetalhes').style.display = 'none';
    }
}



//CARREGAMENTO INFINITO

$(window).scroll(function() {
    if ($(this).scrollTop() + 1 >= $('body').height() - $(window).height()) {
        if (working == false) {
            working = true;

            switch (tela) {
                case 'listarProdutosTodos':
                    listarProdutosTodos();
                    break;
                case 'listarProdutosCategoria':
                    listarProdutosCategoria();
                    break;
                case 'listarProduto':
                    listarProduto();
                    break;
            }

        }
    }
});



listarProdutosTodos();