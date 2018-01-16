function ajaxUrl(el,url){
    // $(el).parent('li').addClass('active');
    $("#load").load(url+' .content-wrapper');
}