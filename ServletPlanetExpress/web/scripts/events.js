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
        alert('Você deve logar para prosseguir com esta ação!!!');
        openLogin(1);
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
    elem = elem.parentNode;

    if (elem.childNodes[4]) {
        elem.childNodes[3].classList.remove('fa-sort-up');
        elem.childNodes[3].classList.add('fa-sort-down');
        elem.removeChild(elem.childNodes[4]);
    } else {
        elem.childNodes[3].classList.remove('fa-sort-down');
        elem.childNodes[3].classList.add('fa-sort-up');
        listarProdutosComprados(elem);
    }

}

function openLogin(i) {
    $('html,body').scrollTop(0);
    if (i == 0) {
        document.getElementById('corpo').style.display = 'none';
        document.getElementsByTagName('body')[0].style.overflowY = 'scroll';
        document.getElementById('formlogreg-interno').childNodes[1].style.display = 'none';
    } else {
        document.getElementById('corpo').style.display = 'inline-block';
        document.getElementsByTagName('body')[0].style.overflow = 'hidden';
        document.getElementById('formlogreg-interno').childNodes[1].style.display = 'inline-block';
    }
}

function openRegistro(i) {
    $('html,body').scrollTop(0);
    if (i == 0) {
        document.getElementById('corpo').style.display = 'none';
        document.getElementsByTagName('body')[0].style.overflowY = 'scroll';
        document.getElementById('formlogreg-interno').childNodes[3].style.display = 'none';
        document.getElementById('formlogreg-interno').childNodes[3].childNodes[1].style.display = 'none';
        document.getElementById('formlogreg-interno').childNodes[3].childNodes[3].style.display = 'none';
    } else if (i == 1) {
        document.getElementById('corpo').style.display = 'inline-block';
        document.getElementsByTagName('body')[0].style.overflow = 'hidden';
        document.getElementById('formlogreg-interno').childNodes[3].style.display = 'inline-block';
        document.getElementById('formlogreg-interno').childNodes[3].childNodes[1].style.display = 'inline-block';
    } else if (i == 2) {
        document.getElementById('corpo').style.display = 'inline-block';
        document.getElementsByTagName('body')[0].style.overflow = 'hidden';
        document.getElementById('formlogreg-interno').childNodes[3].style.display = 'inline-block';
        document.getElementById('formlogreg-interno').childNodes[3].childNodes[3].style.display = 'inline-block';
        carregaEstados(document.querySelector('select#estado'));
    }
}

//USER
function edicaodecampos(o) {
    let elemento = document.getElementById('dadosUser').childNodes;

    if (o == 0) {
        for (let i = 0; i < (elemento.length - 5); i++) {
            elemento[i].readOnly = true;
        }
    } else {
        for (let i = 0; i < (elemento.length - 5); i++) {
            elemento[i].readOnly = false;
        }
    }

}

function edicaodeendereco(o) {
    let elemento = document.getElementById('dadosEndereco').childNodes;

    if (o == 0) {
        for (let i = 0; i < elemento.length; i++) {
            elemento[i].readOnly = true;
        }

        document.getElementById('userestado').disabled = true;
    } else {
        for (let i = 0; i < elemento.length; i++) {
            elemento[i].readOnly = false;
        }

        document.getElementById('userestado').disabled = false;
    }

}

//IMAGENS
function trocaimg(elem) {
    elem.parentNode.parentNode.children[1].children[0].src = elem.src;
}

function trocaImagemUser(elem) {
    elem.parentNode.parentNode.children[2].children[0].src = elem.src;
    localStorage.setItem('UserImg', elem.src);
    elem.parentNode.parentNode.children[2].setAttribute('class', elem.classList[0]);
}