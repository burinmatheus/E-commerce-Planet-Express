var pg = 1;
var working = false;
var tela = 'listarProdutosTodos';
var id = 0;
var box = 'boxtodosprodutos';

function requisicao(url, corpo, callbackOk, callbackErro) {
    var http = new XMLHttpRequest();
    http.open("POST", url, true);
    http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    if (window.localStorage.getItem('Authorization') != null) {
        http.setRequestHeader("Authorization", window.localStorage.getItem('Authorization'));
    }

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
    alert(JSON.parse(resp).alerta);
}

function alerterror(resp) {
    alert(`Error: ${JSON.parse(resp).erro}`);
}

//SEGMENTOS

function processaSegmentos(resp) {
    let respostas = JSON.parse(resp).segmentos;

    let barralateral = document.getElementById("barralateral");
    barralateral.innerHTML = ' ';

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
        barralateral.appendChild(segmentobox);

    });
}

function carregaSegmentos() {
    let funcao = 'listaSegCat';
    let caminho = 'EJBPlanetExpress/Tela';

    let corpo = `funcao=${funcao}&caminho=${caminho}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processaSegmentos, alerterror);
}

//ESTADOS
function processaEstados(resp) {
    let respostas = JSON.parse(resp).estados;

    box.innerHTML = '<option value="Estado" selected>Selecione seu Estado</option>';

    respostas.forEach(resposta => {
        let option = document.createElement('option');
        option.value = resposta.id;
        option.innerText = resposta.sigla + ' - ' + resposta.nome;

        box.appendChild(option);
    });

    if (box.id == 'userestado') {
        $('#userestado').val(sessionStorage.getItem('Estado'));
    }
}

function carregaEstados(elem) {
    let funcao = 'listarEstados';
    let caminho = 'EJBPlanetExpress/Tela';
    box = elem;

    let corpo = `funcao=${funcao}&caminho=${caminho}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processaEstados, alerterror);
}


//PROCESSA DADOS RECEBIDOS

function processalistarProduto(resp) {
    let respostas = JSON.parse(resp);
    respostas = respostas.produtos;
    respostaimgs = respostas.imagens;

    let produtodetalhes = document.getElementById('boxprodutodetalhes');
    produtodetalhes.innerHTML = ' ';
    produtodetalhes.setAttribute('class', respostas.id);

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

    //DETALHES
    let boxdetalhes = document.createElement('div');
    boxdetalhes.setAttribute('id', respostas.id);
    boxdetalhes.setAttribute('class', 'boxdetalhes');

    let nomeproduto = document.createElement('h1');
    nomeproduto.setAttribute('id', 'nomeproduto');
    nomeproduto.innerText = respostas.nome;

    let marcaproduto = document.createElement('p');
    marcaproduto.setAttribute('id', 'marcaproduto');
    marcaproduto.innerText = respostas.marca;

    boxdetalhes.appendChild(nomeproduto);
    boxdetalhes.appendChild(marcaproduto);

    if (respostas.desconto != 0) {
        let valorA = document.createElement('p');
        valorA.classList.add('produtovalorA');
        valorA.innerHTML = "R$: " + respostas.valor.toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        boxdetalhes.appendChild(valorA);

        let valorB = document.createElement('p');
        valorB.classList.add('produtovalorB');
        valorB.innerHTML = "Por R$: " + (respostas.valor - (respostas.valor * (respostas.desconto / 100))).toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        boxdetalhes.appendChild(valorB);

        let descontos = document.createElement('span');
        descontos.classList.add('produtodesconto');
        descontos.innerHTML = respostas.desconto + "% de desconto";

        boxdetalhes.appendChild(descontos);
    } else {
        let valorA = document.createElement('p');
        valorA.classList.add('produtovalor');
        valorA.innerHTML = "R$: " + respostas.valor.toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        boxdetalhes.appendChild(valorA);
    }

    let btnfav = document.createElement('div');
    btnfav.classList.add('btnaddFav');
    btnfav.innerHTML = "Adicionar aos Favoritos"
    btnfav.setAttribute('onclick', 'adicionarFavoritos(this)');
    boxdetalhes.appendChild(btnfav);

    let btn = document.createElement('div');
    btn.classList.add('btnaddCarrinho');
    btn.innerHTML = "Adicionar ao Carrinho"
    btn.setAttribute('onclick', 'addCarrinho(this)');
    boxdetalhes.appendChild(btn);

    produtodetalhes.appendChild(boxdetalhes);

    //DESCRIÇÃO
    let boxdescricao = document.createElement('div');
    boxdescricao.setAttribute('id', 'boxdescricao');

    let titulodescricao = document.createElement('h2');
    titulodescricao.setAttribute('id', 'titulodescricao');
    titulodescricao.innerText = 'DESCRIÇÃO:';

    let descricao = document.createElement('p');
    descricao.setAttribute('id', 'descricao');
    descricao.innerText = respostas.descricao;

    boxdescricao.appendChild(titulodescricao);
    boxdescricao.appendChild(descricao);
    produtodetalhes.appendChild(boxdescricao);

}

function processalistarProdutos(resp) {
    let respostas = JSON.parse(resp);

    pg = pg + 1;

    let boxprodutos = document.getElementById('' + box + '');

    if (tela == 'listarFavoritos') {
        let titulo = document.createElement('h1');
        titulo.setAttribute('class', 'titulopagina');
        titulo.innerText = respostas.titulo;
        boxprodutos.appendChild(titulo);
    }
    respostas = respostas.produtos;

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
            valorA.innerHTML = "R$: " + resposta.valor.toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
            produtobox.appendChild(valorA);

            let valorB = document.createElement('p');
            valorB.classList.add('produtovalorB');
            valorB.innerHTML = "Por R$: " + (resposta.valor - (resposta.valor * (resposta.desconto / 100))).toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
            produtobox.appendChild(valorB);

            let descontos = document.createElement('span');
            descontos.classList.add('produtodesconto');
            descontos.innerHTML = resposta.desconto + "% de desconto";

            produtobox.appendChild(descontos);
        } else {
            let valorA = document.createElement('p');
            valorA.classList.add('produtovalor');
            valorA.innerHTML = "R$: " + resposta.valor.toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
            produtobox.appendChild(valorA);
        }

        if (tela == 'listarFavoritos') {
            let btnfav = document.createElement('div');
            btnfav.classList.add('btnremoverFav');
            btnfav.innerHTML = "Remover dos Favoritos"
            btnfav.setAttribute('onclick', 'removerFavoritos(this)');
            produtobox.appendChild(btnfav);
        } else {
            let btnfav = document.createElement('div');
            btnfav.classList.add('btnaddFav');
            btnfav.innerHTML = "Adicionar aos Favoritos"
            btnfav.setAttribute('onclick', 'adicionarFavoritos(this)');
            produtobox.appendChild(btnfav);
        }

        let btn = document.createElement('div');
        btn.classList.add('btnaddCarrinho');
        btn.innerHTML = "Adicionar ao Carrinho"
        btn.setAttribute('onclick', 'addCarrinho(this)');


        produtobox.appendChild(btn);
        boxprodutos.appendChild(produtobox);

    });
}

function processalistaComprados(resp) {
    let respostas = JSON.parse(resp);


    let boxprodutos = document.getElementById('boxprodutocomprados');

    if (pg == 1) {
        let titulo = document.createElement('h1');
        titulo.setAttribute('class', 'titulopagina');
        titulo.innerText = respostas.titulo;
        boxprodutos.appendChild(titulo);
    }
    respostas = respostas.pedidos;


    pg = pg + 1;

    respostas.forEach(resposta => {
        let boxcompradoresumo = document.createElement('div');
        boxcompradoresumo.classList.add('boxcompradoresumo');
        boxcompradoresumo.classList.add(resposta.id);
        boxcompradoresumo.setAttribute('id', resposta.id);

        let data = document.createElement('p');
        data.classList.add('datacompra');
        data.innerText = resposta.data;

        let hora = document.createElement('p');
        hora.classList.add('horacompra');
        hora.innerText = resposta.hora;

        let valor = document.createElement('p');
        valor.classList.add('valorcompra');
        valor.innerText = resposta.valor.toLocaleString('pt-br', { style: 'currency', currency: 'BRL', maximumFractionDigits: 2 });

        let i = document.createElement('i');
        i.setAttribute('class', 'fas fa-sort-down');
        i.setAttribute('onclick', 'openprodutoshistorico(this)');


        boxcompradoresumo.appendChild(data);
        boxcompradoresumo.appendChild(hora);
        boxcompradoresumo.appendChild(valor);
        boxcompradoresumo.appendChild(i);

        boxprodutos.appendChild(boxcompradoresumo);
    });
}

function processalistaProdutosComprados(resp) {
    let respostas = JSON.parse(resp);
    respostas = respostas.produtos;

    let boxprodutos = document.createElement('div');
    boxprodutos.setAttribute('class', 'listadeprodutos');

    respostas.forEach(resposta => {
        let produtolistado = document.createElement('div');
        produtolistado.classList.add('produtolistado');
        produtolistado.classList.add(resposta.id);

        let divimg = document.createElement('div');
        divimg.setAttribute('class', 'boximgcomprados');

        let imagem = document.createElement('img');
        imagem.classList.add('imagemProduto');
        imagem.src = "/ServletPlanetExpress/retornaimagem?tipo=produtos&cod=" + resposta.img;

        let nome = document.createElement('h2');
        nome.setAttribute('class', 'nomeproduto');
        nome.innerText = resposta.nome;

        let marca = document.createElement('p');
        marca.setAttribute('class', 'marcaproduto');
        marca.innerText = resposta.marca;

        let qtde = document.createElement('p');
        qtde.setAttribute('class', 'qtdeproduto');
        qtde.innerText = resposta.qtde;

        let btn = document.createElement('div');
        btn.classList.add('verproduto');
        btn.innerHTML = "Ver Produto"
        btn.setAttribute('onclick', 'trocatela(this);');

        divimg.appendChild(imagem);
        produtolistado.appendChild(divimg);
        produtolistado.appendChild(nome);
        produtolistado.appendChild(marca);
        produtolistado.appendChild(qtde);
        produtolistado.appendChild(btn);
        boxprodutos.appendChild(produtolistado);

    });

    pg.appendChild(boxprodutos);
}

function processalistaCarrinho(resp) {
    let respostasJ = JSON.parse(resp);
    let respostas = respostasJ.produtos;

    let valortotal = 0;

    let box = document.getElementById('boxcarrinhodecompras');

    let boxprodutos = document.createElement('table');
    boxprodutos.setAttribute('id', 'tabelaCarrinho');
    boxprodutos.setAttribute('border', '1px');

    if (pg == 1) {
        let titulo = document.createElement('h1');
        titulo.setAttribute('class', 'titulopagina');
        titulo.innerText = respostasJ.titulo;
        box.appendChild(titulo);

        let trcabecalho = document.createElement('thead');
        trcabecalho.setAttribute('id', 'theadCabecalho');
        trcabecalho.innerHTML = '<td>Descrição</td> <td>Preço</td> <td>Quantidade</td> <td>Subtotal</td> <td>Excluir</td>';
        boxprodutos.appendChild(trcabecalho);

    }


    respostas.forEach(resposta => {
        let produtolistado = document.createElement('tr');
        produtolistado.classList.add('produtolistado');
        produtolistado.classList.add(resposta.id);

        //Descrição
        let tdDescricao = document.createElement('td');
        tdDescricao.setAttribute('class', 'tdDescricao');

        let divimg = document.createElement('div');
        divimg.setAttribute('class', 'divimgcarrinho');

        let imagem = document.createElement('img');
        imagem.classList.add('imagemProduto');
        imagem.src = "/ServletPlanetExpress/retornaimagem?tipo=produtos&cod=" + resposta.img;

        let nome = document.createElement('h2');
        nome.classList.add('verproduto');
        nome.setAttribute('class', 'nomeproduto');
        nome.innerText = resposta.nome;
        nome.setAttribute('onclick', 'trocatela(this);');

        let marca = document.createElement('p');
        marca.setAttribute('class', 'marcaproduto');
        marca.innerText = resposta.marca;

        divimg.appendChild(imagem);
        tdDescricao.appendChild(divimg);
        tdDescricao.appendChild(nome);
        tdDescricao.appendChild(marca);


        //Preço
        let tdPreco = document.createElement('td');
        tdPreco.setAttribute('class', 'tdPreco');

        if (resposta.desconto != 0) {
            let valorA = document.createElement('p');
            valorA.classList.add('produtovalorA');
            valorA.innerHTML = "R$: " + resposta.valor.toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
            tdPreco.appendChild(valorA);

            let sifrao = document.createElement('p');
            sifrao.classList.add('produtovalorB');
            sifrao.innerHTML = "Por R$: ";
            tdPreco.appendChild(sifrao);

            let valorB = document.createElement('p');
            valorB.classList.add('produtovalorB');
            valorB.innerHTML = (resposta.valor - (resposta.valor * (resposta.desconto / 100))).toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
            tdPreco.appendChild(valorB);

            let descontos = document.createElement('span');
            descontos.classList.add('produtodesconto');
            descontos.innerHTML = resposta.desconto + "% de desconto";
            tdPreco.appendChild(descontos);

        } else {
            let sifrao = document.createElement('p');
            sifrao.classList.add('produtovalorB');
            sifrao.innerHTML = "R$: ";

            let valorB = document.createElement('p');
            valorB.classList.add('produtovalorB');
            valorB.innerHTML = resposta.valor.toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

            tdPreco.appendChild(sifrao);
            tdPreco.appendChild(valorB);

        }


        //Quantidade
        let tdQuantidade = document.createElement('td');
        tdQuantidade.setAttribute('class', 'tdQuantidade');

        let qtde = document.createElement('input');
        qtde.readOnly = true;
        qtde.setAttribute('class', 'qtdeproduto');
        qtde.value = resposta.qtde;

        let diminuir = document.createElement('i');
        diminuir.setAttribute('class', 'fas fa-caret-square-down');
        diminuir.classList.add('diminuir');
        diminuir.setAttribute('onclick', 'atualizaqtde(this);');

        let aumentar = document.createElement('i');
        aumentar.setAttribute('class', 'fas fa-caret-square-up');
        aumentar.classList.add('aumentar');
        aumentar.setAttribute('onclick', 'atualizaqtde(this);');


        tdQuantidade.appendChild(diminuir);
        tdQuantidade.appendChild(qtde);
        tdQuantidade.appendChild(aumentar);


        //Subtotal
        let tdSubtotal = document.createElement('td');
        tdSubtotal.setAttribute('class', 'tdSubtotal');

        let valorSubtotal = document.createElement('p');
        valorSubtotal.setAttribute('class', 'valorSubtotal');

        if (resposta.desconto != 0) {
            valorSubtotal.innerText = ((resposta.valor - (resposta.valor * (resposta.desconto / 100))) * resposta.qtde).toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
            valortotal = parseFloat(valortotal) + parseFloat((((resposta.valor - (resposta.valor * (resposta.desconto / 100))) * resposta.qtde).toFixed(2)));
        } else {
            valorSubtotal.innerText = (resposta.valor * resposta.qtde).toLocaleString('pt-br', { minimumFractionDigits: 2, maximumFractionDigits: 2 });;
            valortotal = parseFloat(valortotal) + parseFloat((resposta.valor * resposta.qtde));
        }

        tdSubtotal.appendChild(valorSubtotal);


        //Remover
        let tdExcluir = document.createElement('td');
        tdExcluir.setAttribute('class', 'tdExcluir');
        tdExcluir.innerHTML = '<i class="fas fa-window-close excluir" onclick="removeProdutoCarrinho(this);"></i>';


        produtolistado.appendChild(tdDescricao);
        produtolistado.appendChild(tdPreco);
        produtolistado.appendChild(tdQuantidade);
        produtolistado.appendChild(tdSubtotal);
        produtolistado.appendChild(tdExcluir);

        boxprodutos.appendChild(produtolistado);
    });

    let h2total = document.createElement('h2');
    h2total.setAttribute('id', 'h2total');
    h2total.innerText = 'TOTAL R$: ' + valortotal.toLocaleString('pt-br', { minimumFractionDigits: 2 });

    let btnComprar = document.createElement('a');
    btnComprar.setAttribute('class', 'btnComprar');
    btnComprar.setAttribute('onclick', 'fecharPedido();');
    btnComprar.innerText = 'Finalizar Pedido';


    box.appendChild(boxprodutos);
    box.appendChild(h2total);
    box.appendChild(btnComprar);
}

function processalistaDadosUsuario(resp) {
    let respostasJ = JSON.parse(resp);
    let resposta = respostasJ.user;

    let box = document.getElementById('boxuserdados');

    if (pg == 1) {
        let titulo = document.createElement('h1');
        titulo.setAttribute('class', 'titulopagina');
        titulo.innerText = respostasJ.titulo;
        box.appendChild(titulo);

    }

    //BOTÕES
    let divbtn = document.createElement('div');
    divbtn.setAttribute('id', 'btndiv');

    let btnedit = document.createElement('input');
    btnedit.setAttribute('type', 'button');
    btnedit.setAttribute('value', 'Habilitar Edição');
    btnedit.setAttribute('onclick', 'edicaodecampos(1);');

    let btncancelar = document.createElement('input');
    btncancelar.setAttribute('type', 'button');
    btncancelar.setAttribute('value', 'Cancelar Edição');
    btncancelar.setAttribute('onclick', 'listarDadosUsuario();');

    let btnsalvar = document.createElement('input');
    btnsalvar.setAttribute('type', 'button');
    btnsalvar.setAttribute('value', 'Salvar Alterações');
    btnsalvar.setAttribute('onclick', 'salvarDados();');

    divbtn.appendChild(btnedit);
    divbtn.appendChild(btncancelar);
    divbtn.appendChild(btnsalvar);
    box.appendChild(divbtn);

    //IMG
    let imgUser = document.createElement('div');
    imgUser.innerHTML = '<img src="/ServletPlanetExpress/retornaimagem?tipo=users&cod=' + resposta.img + '">';
    imgUser.setAttribute('id', 'userImg');
    imgUser.setAttribute('class', resposta.img);

    //BOX IMGS
    let imgs = document.createElement('div');
    imgs.setAttribute('id', 'imgsUsers');
    imgs.innerHTML = '<img class="Amy" onclick="trocaImagemUser(this);" src="/ServletPlanetExpress/retornaimagem?tipo=users&cod=Amy">' +
        '<img class="Bender" onclick="trocaImagemUser(this);" src="/ServletPlanetExpress/retornaimagem?tipo=users&cod=Bender">' +
        '<img class="Farnsworth" onclick="trocaImagemUser(this);" src="/ServletPlanetExpress/retornaimagem?tipo=users&cod=Farnsworth">' +
        '<img class="Fry" onclick="trocaImagemUser(this);" src="/ServletPlanetExpress/retornaimagem?tipo=users&cod=Fry">' +
        '<img class="Hermes" onclick="trocaImagemUser(this);" src="/ServletPlanetExpress/retornaimagem?tipo=users&cod=Hermes">' +
        '<img class="Leela" onclick="trocaImagemUser(this);" src="/ServletPlanetExpress/retornaimagem?tipo=users&cod=Leela">' +
        '<img class="Nibbler" onclick="trocaImagemUser(this);" src="/ServletPlanetExpress/retornaimagem?tipo=users&cod=Nibbler">' +
        '<img class="Zoidberg" onclick="trocaImagemUser(this);" src="/ServletPlanetExpress/retornaimagem?tipo=users&cod=Zoidberg">';


    //DADOS USUÁRIO
    let dadosUser = document.createElement('div');
    dadosUser.setAttribute('id', 'dadosUser');

    //Nome
    let labelnome = document.createElement('label');
    labelnome.setAttribute('id', 'labelnome');
    labelnome.innerText = 'Nome:';

    let usernome = document.createElement('input');
    usernome.setAttribute('id', 'usernome');
    usernome.setAttribute('class', 'campodadouser');
    usernome.value = resposta.nome;

    dadosUser.appendChild(labelnome);
    dadosUser.appendChild(usernome);

    //Sobrenome
    let labelsobrenome = document.createElement('label');
    labelsobrenome.setAttribute('id', 'labelsobrenome');
    labelsobrenome.innerText = 'Sobrenome:';

    let usersobrenome = document.createElement('input');
    usersobrenome.setAttribute('id', 'usersobrenome');
    usersobrenome.setAttribute('class', 'campodadouser');
    usersobrenome.value = resposta.sobrenome;

    dadosUser.appendChild(labelsobrenome);
    dadosUser.appendChild(usersobrenome);

    //Email
    let labelemail = document.createElement('label');
    labelemail.setAttribute('id', 'labelemail');
    labelemail.innerText = 'Email:';

    let useremail = document.createElement('input');
    useremail.setAttribute('id', 'useremail');
    useremail.setAttribute('class', 'campodadouser');
    useremail.value = resposta.email;

    dadosUser.appendChild(labelemail);
    dadosUser.appendChild(useremail);

    //Telefone
    let labeltelefone = document.createElement('label');
    labeltelefone.setAttribute('id', 'labeltelefone');
    labeltelefone.innerText = 'Telefone:';

    let usertelefone = document.createElement('input');
    usertelefone.setAttribute('id', 'usertelefone');
    usertelefone.setAttribute('class', 'campodadouser');
    usertelefone.setAttribute('type', 'number');
    usertelefone.value = resposta.telefone;

    dadosUser.appendChild(labeltelefone);
    dadosUser.appendChild(usertelefone);

    //RG
    let labelrg = document.createElement('label');
    labelrg.setAttribute('id', 'labelrg');
    labelrg.innerText = 'RG:';

    let userrg = document.createElement('input');
    userrg.setAttribute('id', 'userrg');
    userrg.setAttribute('class', 'campodadouser');
    userrg.setAttribute('type', 'number');
    userrg.setAttribute('onclick', 'alert(\'Para Alterar este dado entre em contato conosco!\')');
    userrg.readOnly = true;
    userrg.value = resposta.rg;

    dadosUser.appendChild(labelrg);
    dadosUser.appendChild(userrg);

    //CPF
    let labelcpf = document.createElement('label');
    labelcpf.setAttribute('id', 'labelcpf');
    labelcpf.innerText = 'CPF:';

    let usercpf = document.createElement('input');
    usercpf.setAttribute('id', 'usercpf');
    usercpf.setAttribute('class', 'campodadouser');
    usercpf.setAttribute('type', 'number');
    usercpf.setAttribute('onclick', 'alert(\'Para Alterar este dado entre em contato conosco!\')');
    usercpf.readOnly = true;
    usercpf.value = resposta.cpf;

    dadosUser.appendChild(labelcpf);
    dadosUser.appendChild(usercpf);

    //Senha
    let labelsenha = document.createElement('label');
    labelsenha.setAttribute('id', 'labelsenha');
    labelsenha.innerText = 'Senha:';

    let usersenha = document.createElement('input');
    usersenha.setAttribute('id', 'usersenha');
    usersenha.setAttribute('class', 'campodadouser');
    usersenha.setAttribute('type', 'password');

    let labelconfsenha = document.createElement('label');
    labelconfsenha.setAttribute('id', 'labelconfsenha');
    labelconfsenha.innerText = 'Repita a senha:';

    let userconfsenha = document.createElement('input');
    userconfsenha.setAttribute('id', 'userconfsenha');
    userconfsenha.setAttribute('class', 'campodadouser');
    userconfsenha.setAttribute('type', 'password');

    dadosUser.appendChild(labelsenha);
    dadosUser.appendChild(usersenha);

    dadosUser.appendChild(labelconfsenha);
    dadosUser.appendChild(userconfsenha);

    let btnsenha = document.createElement('input');
    btnsenha.setAttribute('class', 'campodadouser');
    btnsenha.setAttribute('type', 'button');
    btnsenha.setAttribute('onclick', 'salvaSenha();');
    btnsenha.value = '>';

    dadosUser.appendChild(btnsenha);



    box.appendChild(imgUser);
    box.appendChild(imgs);
    box.appendChild(dadosUser);

    edicaodecampos(0);
    listarDadosEndereco();
}

function processalistaEnderecoUsuario(resp) {
    let respostasJ = JSON.parse(resp);
    let resposta = respostasJ.endereco;

    let box = document.getElementById('boxuserdados');

    if (pg == 1) {
        let titulo = document.createElement('h1');
        titulo.setAttribute('class', 'titulopagina');
        titulo.innerText = respostasJ.titulo;
        box.appendChild(titulo);

    }

    //BOTÕES
    let divbtn = document.createElement('div');
    divbtn.setAttribute('id', 'btndivendereco');

    let btnedit = document.createElement('input');
    btnedit.setAttribute('type', 'button');
    btnedit.setAttribute('value', 'Editar Endereço');
    btnedit.setAttribute('onclick', 'edicaodeendereco(1);');

    let btncancelar = document.createElement('input');
    btncancelar.setAttribute('type', 'button');
    btncancelar.setAttribute('value', 'Cancelar Edição');
    btncancelar.setAttribute('onclick', 'listarDadosUsuario();');

    let btnsalvar = document.createElement('input');
    btnsalvar.setAttribute('type', 'button');
    btnsalvar.setAttribute('value', 'Salvar Endereço');
    btnsalvar.setAttribute('onclick', 'salvarEndereco();');

    divbtn.appendChild(btnedit);
    divbtn.appendChild(btncancelar);
    divbtn.appendChild(btnsalvar);
    box.appendChild(divbtn);

    //DADOS USUÁRIO
    let dadosEndereco = document.createElement('div');
    dadosEndereco.setAttribute('id', 'dadosEndereco');

    //Estado
    let labelestado = document.createElement('label');
    labelestado.setAttribute('id', 'labelestado');
    labelestado.innerText = 'Estado:';

    let userestado = document.createElement('select');
    userestado.setAttribute('id', 'userestado');
    userestado.setAttribute('class', 'campodadouser');
    sessionStorage.setItem('Estado', resposta.estado_id);

    dadosEndereco.appendChild(labelestado);
    dadosEndereco.appendChild(userestado);

    //Cidade
    let labelcidade = document.createElement('label');
    labelcidade.setAttribute('id', 'labelcidade');
    labelcidade.innerText = 'Cidade:';

    let usercidade = document.createElement('input');
    usercidade.setAttribute('id', 'usercidade');
    usercidade.setAttribute('class', 'campodadouser');
    usercidade.value = resposta.cidade;

    dadosEndereco.appendChild(labelcidade);
    dadosEndereco.appendChild(usercidade);

    //CEP
    let labelcep = document.createElement('label');
    labelcep.setAttribute('id', 'labelcep');
    labelcep.innerText = 'CEP:';

    let usercep = document.createElement('input');
    usercep.setAttribute('id', 'usercep');
    usercep.setAttribute('class', 'campodadouser');
    usercep.value = resposta.cep;

    dadosEndereco.appendChild(labelcep);
    dadosEndereco.appendChild(usercep);

    //Bairro
    let labelbairro = document.createElement('label');
    labelbairro.setAttribute('id', 'labelbairro');
    labelbairro.innerText = 'Bairro:';

    let userbairro = document.createElement('input');
    userbairro.setAttribute('id', 'userbairro');
    userbairro.setAttribute('class', 'campodadouser');
    userbairro.value = resposta.bairro;

    dadosEndereco.appendChild(labelbairro);
    dadosEndereco.appendChild(userbairro);

    //Rua
    let labelrua = document.createElement('label');
    labelrua.setAttribute('id', 'labelrua');
    labelrua.innerText = 'Rua:';

    let userrua = document.createElement('input');
    userrua.setAttribute('id', 'userrua');
    userrua.setAttribute('class', 'campodadouser');
    userrua.value = resposta.rua;

    dadosEndereco.appendChild(labelrua);
    dadosEndereco.appendChild(userrua);

    //Numero
    let labelnumero = document.createElement('label');
    labelnumero.setAttribute('id', 'labelnumero');
    labelnumero.innerText = 'Número:';

    let usernumero = document.createElement('input');
    usernumero.setAttribute('id', 'usernumero');
    usernumero.setAttribute('class', 'campodadouser');
    usernumero.setAttribute('type', 'number');
    usernumero.value = resposta.numero;

    dadosEndereco.appendChild(labelnumero);
    dadosEndereco.appendChild(usernumero);


    box.appendChild(dadosEndereco);

    edicaodeendereco(0);
    carregaEstados(document.getElementById('userestado'));

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

    let corpo = `funcao=${funcao}&caminho=${caminho}&id=${id}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistarProduto, alerterror);
}

function listarFavoritos() {
    let funcao = 'listarFavoritos';
    let caminho = 'EJBPlanetExpress/Favoritos';
    funE = "listarFavoritos";

    if (pg == 1) {
        let boxprodutos = document.getElementById("boxprodutofavoritos");

        boxprodutos.innerHTML = '  ';

    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&pg=${pg}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistarProdutos, alerterror);
}

function listarComprados() {
    let funcao = 'listarPedidosFinalizados';
    let caminho = 'EJBPlanetExpress/Pedidos';
    funE = "listarPedidosFinalizados";

    if (pg == 1) {
        let boxprodutos = document.getElementById("boxprodutocomprados");

        boxprodutos.innerHTML = '  ';

    }

    let corpo = `funcao=${funcao}&caminho=${caminho}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistaComprados, alerterror);
}

function listarProdutosComprados(elem) {
    let funcao = 'listarProdutosPedidoF';
    let caminho = 'EJBPlanetExpress/Pedidos';
    funE = "listarProdutosPedidoF";
    pg = elem;

    let corpo = `funcao=${funcao}&caminho=${caminho}&id_pedido=${elem.id}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistaProdutosComprados, alerterror);
}

function listarCarrinho() {
    let funcao = 'listarProdutosPedidoA';
    let caminho = 'EJBPlanetExpress/Pedidos';
    funE = "listarProdutosPedidoA";

    if (pg == 1) {
        let box = document.getElementById("boxcarrinhodecompras");

        box.innerHTML = '  ';

    }

    let corpo = `funcao=${funcao}&caminho=${caminho}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistaCarrinho, alerterror);
}

function listarDadosEndereco() {
    let funcao = 'dadosEndereco';
    let caminho = 'EJBPlanetExpress/User';
    funE = "dadosEndereco";

    let cpf;

    if (document.getElementById('usercpf').value != '') {
        cpf = document.getElementById('usercpf').value;

    } else {
        alert('Campo de CPF em branco !!!');
        return false;
    }

    let corpo = `funcao=${funcao}&caminho=${caminho}&cpf=${cpf}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistaEnderecoUsuario, alerterror);
}

function listarDadosUsuario() {
    let funcao = 'dadosUsuario';
    let caminho = 'EJBPlanetExpress/User';
    funE = "dadosUsuario";

    if (pg == 1) {
        let box = document.getElementById("boxuserdados");

        box.innerHTML = '  ';

    }

    let corpo = `funcao=${funcao}&caminho=${caminho}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, processalistaDadosUsuario, alerterror);
}

//NAVIGATOR
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

    } else if (identificacao == 'verproduto') {
        trocadisplay('produtodetalhes');
        pg = 1;
        tela = 'listarProduto';
        box = 'produtodetalhes';
        id = elem.parentNode.classList[1];
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
        document.getElementById('campopesquisa').value = '';
        document.getElementById('campopesquisa').style.display = 'none';
        listarProdutosPesquisa();

    } else if (identificacao == 'favoritos') {
        trocadisplay('boxprodutofavoritos');
        pg = 1;
        tela = 'listarFavoritos';
        box = 'boxprodutofavoritos';
        listarFavoritos();
        openpopup();

    } else if (identificacao == 'meusdados') {
        trocadisplay('boxuserdados');
        pg = 1;
        tela = 'dadosUsuario';
        box = 'boxuserdados';
        listarDadosUsuario();
        openpopup();

    } else if (identificacao == 'comprados') {
        trocadisplay('boxprodutocomprados');
        pg = 1;
        tela = 'listarPedidosFinalizados';
        box = 'boxprodutocomprados';
        listarComprados();
        openpopup();

    } else if (identificacao == 'carrinho') {
        if (localStorage.getItem('Authorization') != null) {
            trocadisplay('boxcarrinhodecompras');
            pg = 1;
            tela = 'listarProdutosPedidoA';
            box = 'boxcarrinhodecompras';
            listarCarrinho();
        } else {
            alert('Você deve logar para prosseguir com esta ação!!!');
            openLogin(1);
        }

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

    if (a == 'boxprodutofavoritos') {
        document.getElementById('produtofavoritos').style.display = 'block';
    } else {
        document.getElementById('produtofavoritos').style.display = 'none';
    }

    if (a == 'boxprodutocomprados') {
        document.getElementById('boxprodutocomprados').style.display = 'block';
    } else {
        document.getElementById('boxprodutocomprados').style.display = 'none';
    }

    if (a == 'boxcarrinhodecompras') {
        document.getElementById('boxcarrinhodecompras').style.display = 'block';
    } else {
        document.getElementById('boxcarrinhodecompras').style.display = 'none';
    }

    if (a == 'boxuserdados') {
        document.getElementById('boxuserdados').style.display = 'block';
    } else {
        document.getElementById('boxuserdados').style.display = 'none';
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
                case 'listarProdutosPesquisa':
                    listarProdutosPesquisa();
                    break;
            }

        }
    }
});



listarProdutosTodos();