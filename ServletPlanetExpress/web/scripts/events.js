//HEADER
function openpopup() {
    let elemento = document.querySelector('span.popup');

    if (elemento.style.display == '' || elemento.style.display == 'none') {
        elemento.style.display = 'block';
    } else {
        elemento.style.display = 'none';
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