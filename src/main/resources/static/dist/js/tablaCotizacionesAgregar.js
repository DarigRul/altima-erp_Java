var table;

(function() {
  var _div = document.createElement('div');
  jQuery.fn.dataTable.ext.type.search.html = function(data) {
    _div.innerHTML = data;
    return _div.textContent ?
      _div.textContent
        .replace(/[áÁàÀâÂäÄãÃåÅæÆ]/g, 'a')
        .replace(/[çÇ]/g, 'c')
        .replace(/[éÉèÈêÊëË]/g, 'e')
        .replace(/[íÍìÌîÎïÏîĩĨĬĭ]/g, 'i')
        .replace(/[ñÑ]/g, 'n')
        .replace(/[óÓòÒôÔöÖœŒ]/g, 'o')
        .replace(/[ß]/g, 's')
        .replace(/[úÚùÙûÛüÜ]/g, 'u')
        .replace(/[ýÝŷŶŸÿ]/g, 'n') :
      _div.innerText
        .replace(/[áÁàÀâÂäÄãÃåÅæÆ]/g, 'a')
        .replace(/[çÇ]/g, 'c')
        .replace(/[éÉèÈêÊëË]/g, 'e')
        .replace(/[íÍìÌîÎïÏîĩĨĬĭ]/g, 'i')
        .replace(/[ñÑ]/g, 'n')
        .replace(/[óÓòÒôÔöÖœŒ]/g, 'o')
        .replace(/[ß]/g, 's')
        .replace(/[úÚùÙûÛüÜ]/g, 'u')
        .replace(/[ýÝŷŶŸÿ]/g, 'n');
  };
})();
$(document).ready(function() {
	    table = $('#tablaCotizacionesAgregar')
	        .DataTable({
	            "ordering": false,
	            "pageLength": 5,
	            "responsive": true,
	            "stateSave":true,
	            "drawCallback": function() {
	                $('.popoverxd').popover({
	                    container: 'body',
	                    trigger: 'hover'
	                });
	              },
	              "columnDefs": [{
	                  "type": "html",
	                  "targets": '_all'
	                }],
	            "lengthMenu": [
	                [5, 10, 25, 50, 100],
	                [5, 10, 25, 50, 100]
	            ],
	            "language": {
	                "sProcessing": "Procesando...",
	                "sLengthMenu": "Mostrar _MENU_ registros",
	                "sZeroRecords": "No se encontraron resultados",
	                "sEmptyTable": "Ningún dato disponible en esta tabla =(",
	                "sInfo": "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
	                "sInfoEmpty": "Mostrando registros del 0 al 0 de un total de 0 registros",
	                "sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
	                "sInfoPostFix": "",
	                "sSearch": "Buscar:",
	                "sUrl": "",
	                "sInfoThousands": ",",
	                "sLoadingRecords": "Cargando...",
	                "oPaginate": {
	                    "sFirst": "Primero",
	                    "sLast": "Último",
	                    "sNext": "Siguiente",
	                    "sPrevious": "Anterior"
	                },
	                "oAria": {
	                    "sSortAscending": ": Activar para ordenar la columna de manera ascendente",
	                    "sSortDescending": ": Activar para ordenar la columna de manera descendente"
	                },
	                "buttons": {
	                    "copy": "Copiar",
	                    "colvis": "Visibilidad"
	                }
	            }
	        });
	    new $.fn.dataTable.FixedHeader(table);


	    if($('#tipoCotizacion').val()==1){
			table.columns(':eq(0)').visible(false);
			table.columns(':eq(1)').visible(false);
			table.columns(':eq(3)').visible(false);
			table.columns(':eq(4)').visible(false);
			table.columns(':eq(6)').visible(false);
			table.columns(':eq(12)').visible(false);
			$('#GeneralDesglosada').text("Familia de composición");
			$('.cantidadCotizacion').hide();
			$('.coordinadoCotizacion').hide();
			$('#tamanoSelect').removeClass('form-group col-md-3');
			$('#tamanoSelect').addClass('form-group col-md-1');
			$('.modeloCotizacion').hide();
			
		}
	    else if($('#tipoCotizacion').val()==2){
	    	table.columns().visible(true);
	    	$('#GeneralDesglosada').text("Tela");
	    	$('.cantidadCotizacion').show();
			$('.coordinadoCotizacion').show();
			$('#tamanoSelect').addClass('form-group col-md-1');
			$('#tamanoSelect').addClass('form-group col-md-3');
			$('.modeloCotizacion').show();
			
	    }
	    else{
	    	table.columns(':eq(0)').visible(true);
	    	table.columns(':eq(1)').visible(true);
	    	table.columns(':eq(12)').visible(true);
	    	$('.cantidadCotizacion').show();
	    	$('.coordinadoCotizacion').show();
	    	$('.modeloCotizacion').hide();
	    	$('#tamanoSelect').removeClass('form-group col-md-3');
	    	$('#tamanoSelect').addClass('form-group col-md-1');
	    	$('#GeneralDesglosada').text("Familia de composición");
	    	table.columns(':eq(3)').visible(false);
	    	table.columns(':eq(4)').visible(false);
	    	table.columns(':eq(6)').visible(false);
	    }
	    
	  	$('#cotizacionNueva').show();
	  	 table.draw();
	  	$('#tablaCotizacionesAgregar_wrapper').css('width', '100%');
	  	
});
// Remove accented character from search input as well
$('#example_filter input[type=search]').keyup( function () {
    var table = $('#tablaCotizacionesAgregar').DataTable(); 
    table.search(
        jQuery.fn.DataTable.ext.type.search.html(this.value)
    ).draw();
} );

