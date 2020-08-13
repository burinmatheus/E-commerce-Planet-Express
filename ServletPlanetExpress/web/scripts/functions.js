function removerFavoritos(elem) {
    let funcao = 'removerFavoritos';
    let caminho = 'EJBPlanetExpress/Favoritos';
    let idproduto = elem.parentNode.id;

    let corpo = `funcao=${funcao}&caminho=${caminho}&id_produto=${idproduto}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);

    pg == 1;
    listarFavoritos();
}

function adicionarFavoritos(elem) {
    let funcao = 'adicionarFavoritos';
    let caminho = 'EJBPlanetExpress/Favoritos';
    let idproduto = elem.parentNode.id;

    let corpo = `funcao=${funcao}&caminho=${caminho}&id_produto=${idproduto}`;
    requisicao('/ServletPlanetExpress/ServletPlanetExpress', corpo, alertresposta, alerterror);
}