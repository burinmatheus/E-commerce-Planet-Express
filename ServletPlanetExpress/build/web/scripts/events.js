//HEADER
function openpopup() {
    let elemento = document.querySelector('span.popup');

    if (localStorage.getItem('Authorization') != null) {

        if (elemento.style.display == '' || elemento.style.display == 'none') {
            elemento.style.display = 'block';
        } else {
            elemento.style.display = 'none';
        }
    } else {
        //TROCAR PARA LOGIN
        alert('Você deve logar para prosseguir com esta ação!!!');
    }
}

function opensearch(elem) {
    let campo = document.getElementById('campopesquisa');

    if (campo.value != '') {
        trocatela(elem);
    } else {
        if (campo.style.display == '' || campo.style.display == 'none') {
            campo.style.display = 'inline-block';
        } else {
            campo.style.display = 'none';
        }
    }
}

function opencatgorias() {
    let barralateral = document.querySelector('div#barralateral');

    if (barralateral.childElementCount == 0) {
        carregaSegmentos();
    } else {
        if (barralateral.style.display == '' || barralateral.style.display == 'none') {
            barralateral.style.display = 'block';
        } else {
            barralateral.style.display = 'none';
        }
    }
}

function openprodutoshistorico(elem) {

    if (elem.childNodes[3].classList[2] == 'aberto') {
        elem.childNodes[3].classList.remove('fa-sort-down');
        elem.childNodes[3].classList.add('fa-sort-up');
        elem.removeChild(elem.childNodes[4]);
    } else {
        elem.childNodes[3].classList.remove('fa-sort-up');
        elem.childNodes[3].classList.add('fa-sort-down');
        elem.childNodes[3].classList.add('aberto');
        listaProdutosHistorico(elem.id);
    }

}

//IMAGENS
function trocaimg(elem) {
    elem.parentNode.parentNode.children[1].children[0].src = elem.src;
}