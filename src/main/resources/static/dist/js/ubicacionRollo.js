var table;
var app = new Vue({
    el: '#app',
    data: {
        return: {

        }
    },
    mounted() {
        $('.table thead tr').clone(true).appendTo('.table thead');
        $('.table thead tr:eq(0) th').each(function () {
            var title = $('.table thead th').eq($(this).index()).text();
            $(this).html('<input type="text" class="form-control" placeholder="Buscar" />');
        });
        table = $('.table')
            .DataTable({
                "ordering": false,
                "orderCellsTop": true,
                "fixedHeader": true,
                "pageLength": 5,
                "stateSave": true,
                "responsive": true,
                "drawCallback": function () {
                    $('.popoverxd').popover({
                        container: 'body',
                        trigger: 'hover'
                    });
                },
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
        // new $.fn.dataTable.FixedHeader(table);
        // Restore state
        var state = table.state.loaded();
        if (state) {
            table.columns().eq(0).each(function (colIdx) {
                var colSearch = state.columns[colIdx].search;

                if (colSearch.search) {
                    $('input', table.column(colIdx).header()).val(colSearch.search);
                }
            });

            table.draw();
        }


        // Apply the search
        table.columns().eq(0).each(function (colIdx) {
            $('input', table.column(colIdx).header()).on('keyup change', function () {
                table
                    .column(colIdx)
                    .search(this.value)
                    .draw();
            });
        });
    },
    methods: {
        getRowsFiltered: function () {
            var array = [];
            table.column(1, { search: 'applied' }).data().each(function (value, index) {
                var temp = {
                    "idText": value
                }
                array.push(temp);
            });
            const params = new URLSearchParams();
            params.append('listIdText', JSON.stringify(array));
            params.append('format', "pdf");
            console.log(params);
            window.open("/rollotelabarcode?" + params);
            console.log(array);
        }
    }
})