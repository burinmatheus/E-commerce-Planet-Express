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

function processaSegmentos(resp) {
    let respostas = JSON.parse(resp).segmentos;

    let barraLateral = document.getElementById("barraLateral");
    barraLateral.innerHTML = ' ';

    respostas.forEach(resposta => {
        let segmentobox = document.createElement('div');
        segmentobox.classList.add('segmentobox');
        segmentobox.setAttribute('id', resposta.idS);

        let imagem = document.createElement('img');
        imagem.classList.add('imagemSegmento');
        imagem.src = "./img/segmentos/" + resposta.imgS + ".svg"

        let p = document.createElement('p');
        p.classList.add('nomeSegmento');
        p.innerHTML = resposta.nomeS;

        let icon = document.createElement('i');
        icon.classList.add('fas');
        icon.classList.add('fa-chevron-down');

        let categoriabox = document.createElement('div');
        categoriabox.classList.add('categoriabox');

        respostas.categorias.forEach(cat => {
            let pc = document.createElement('p');
            pc.classList.add(cat.idC);
            pc.innerHTML = cat.nomeC;

            categoriabox.appendChild(pc);
        });

        segmentobox.appendChild(imagem);
        segmentobox.appendChild(p);
        segmentobox.appendChild(icon);
        segmentobox.appendChild(categoriabox);
        barraLateral.appendChild(segmentobox);

    });


    function carregaSegmentos() {
        let funcao = 'listaSegCat';
        let caminho = 'EJBPlanetExpress/Tela';

        let corpo = "";
        requisicao('/donotforget/ServletDNF', corpo, processaSegmentos, alerterror);
    }


    carregaSegmentos();


}