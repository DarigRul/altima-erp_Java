var ordenCabecero = []
var ordenDetalle = []
$(document).ready(function () {
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = mm + '/' + dd + '/' + yyyy;
    $("#curdate").text(today);
    $.ajax({
        type: "GET",
        url: "/getComercialLookupByTipo",
        data: {
            tipoLookup: 'iva'
        },
        success: function (response) {
            response.forEach(data => {
                $(`#selectIva`).append(`<option value='${data.atributo1}'>${data.atributo1}%</option>`)
            });
            $(`#selectIva`).selectpicker('refresh');
        }
    });
    if (cabecero !== null) {
        $('#rfc').text(proveedor.rfcProveedor);
        $('#calle').text(proveedor.calle);
        $('#noext').text(proveedor.numeroExterior);
        $('#noint').text(proveedor.numeroInterior);
        $('#colonia').text(proveedor.colonia);
        $('#cp').text(proveedor.codigoPostal);
        $('#poblacion').text(proveedor.poblacion);
        $('#pais').text(proveedor.pais);
        $("#proveedorSelect").append(`<option selected value="${proveedor.idProveedor}">${proveedor.nombreProveedor}</option>`)
        $("#proveedorSelect").attr("disabled",true)
        $('#proveedorSelect').change()
        $('#proveedorSelect').selectpicker('refresh');
        


    }
});

$("#proveedorSelect").change(function (e) {
    e.preventDefault();
    $('#claveMaterialProveedor option').remove();
    $('#claveMaterialProveedor').selectpicker('refresh');
    var id = $("#proveedorSelect").val();
    $.ajax({
        type: "GET",
        url: `/proveedor/${id}`,
        success: function (response) {
            $('#rfc').text(response.rfcProveedor);
            $('#calle').text(response.calle);
            $('#noext').text(response.numeroExterior);
            $('#noint').text(response.numeroInterior);
            $('#colonia').text(response.colonia);
            $('#cp').text(response.codigoPostal);
            $('#poblacion').text(response.poblacion);
            $('#pais').text(response.pais);
        },
        error: function (response) {
            console.log(response)
        }
    });
    $.ajax({
        type: "GET",
        url: `/materiales_all_by_proveedor/${id}`,
        success: function (response) {
            $("#claveMaterialProveedor").append(`<option value="">Seleccione uno...</option>`)
            response.forEach(function (data) {
                //aqui va el codigo
                $("#claveMaterialProveedor").append(`<option data-almacen='${data.almacen}' data-precio='${data.precio}' data-tipo='${data.tipo}' data-idtext='${data.id_text}' data-nombre='${data.nombre}' data-color='${data.color}' value='${data.id_material}'>${data.id_text} - ${data.nombre} </option>`)
            })
            $('#claveMaterialProveedor').selectpicker('refresh');
        },
        error: function (response) {
            console.log(response)
        }
    });
});

$('#agregarMaterial').click(function (e) {
    e.preventDefault();
    $(this).attr('disabled', true);
    $(`#proveedorSelect`).attr('disabled', true);
    $('#proveedorSelect').selectpicker('refresh');
    var cantidad = $('#cantidadProveedor').val();
    var idMaterial = $('#claveMaterialProveedor').val();
    var color = $('#claveMaterialProveedor').children('option:selected').data('color') == '' ? $('#coloresProveedor').children('option:selected').data('name') : $('#claveMaterialProveedor').children('option:selected').data('color')
    var idText = $('#claveMaterialProveedor').children('option:selected').data('idtext')
    var nombre = $('#claveMaterialProveedor').children('option:selected').data('nombre')
    var tipo = $('#claveMaterialProveedor').children('option:selected').data('tipo')
    var idColor = $("#coloresProveedor").val() === '' ? 0 : $("#coloresProveedor").val();
    var precio = $(`#claveMaterialProveedor`).children('option:selected').data('precio');
    var almacen = $(`#claveMaterialProveedor`).children('option:selected').data('almacen');
    const found = ordenDetalle.find(element => element.idMaterial + element.tipo + element.idColor == idMaterial + tipo + idColor);
    if (found != null) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Registro duplicado!',
        }).then(result => {
            $(this).attr('disabled', false);
        })
        return false;
    }

    console.log(idMaterial);
    if (cantidad.trim() === '' || idMaterial.trim() === '' || cantidad <= 0) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Todos los campos deben de estar llenos!',
        }).then(result => {
            $(this).attr('disabled', false);
        })

    }
    else {
        temp = {
            'cantidad': cantidad,
            'idMaterial': idMaterial,
            'tipo': tipo,
            'idColor': idColor,
            'precioU': precio,
            'montoCD': 0
        }
        ordenDetalle.push(temp);
        var fila = tableOrden.row.add(
            [cantidad,
                idText,
                nombre,
                color == "" ? 'N/A' : color,
                almacen,
                `<input type="number"  class="form-control" value='${precio}' id="precio-${idMaterial}-${tipo}-${idColor}" onInput="precioU('${idMaterial}-${tipo}-${idColor}',this.value,${cantidad})">`,
                `<input type="number" class="form-control" id="monto-${idMaterial}-${tipo}-${idColor}" value='0' onInput="monto('${idMaterial}-${tipo}-${idColor}',this.value)" > `,
                `<input type="number" class="form-control" id="porcentaje-${idMaterial}-${tipo}-${idColor}" value='0' onInput="porcentaje('${idMaterial}-${tipo}-${idColor}',this.value)" > `,
                `<p class="subtotal" id="subtotal-${idMaterial}-${tipo}-${idColor}">${precio * cantidad}</p>`,
                `<a class="btn btn-danger btn-circle btn-sm delete" onclick="deleteMovimiento(this,'${idMaterial}${tipo}${idColor}')"><i class="fas fa-times text-white"></i></a>`
            ]
        ).draw();
        $(this).attr('disabled', false);
        totales();
    }
});

$("#claveMaterialProveedor").change(function (e) {
    e.preventDefault();
    var color = $(this).children('option:selected').data('color')
    console.log(color)
    $('#coloresProveedor option').remove();
    $("#coloresProveedor").append(`<option value="" data-name="">Seleccione uno...</option>`)
    $('#coloresProveedor').selectpicker('refresh');
    if (color.trim() === '') {
        $('#coloresProveedor').attr('disabled', false);
        $('#coloresProveedor').selectpicker('refresh');
        $.ajax({
            type: "GET",
            url: "/getDisenioLookupByTipo",
            data: {
                tipo: 'color'
            },
            success: function (response) {
                console.log(response)
                response.forEach(function (data) {
                    //aqui va el codigo
                    $("#coloresProveedor").append(`<option data-content="<i class='fa fa-tint' style='color:${data.atributo1};'></i>  ${data.nombreLookup}" value='${data.idLookup}' data-name='${data.nombreLookup}' ></option>`)
                })
                $('#coloresProveedor').selectpicker('refresh');
            },
            error: function (response) {

            }
        });
    }
    else {
        $('#coloresProveedor').attr('disabled', true);
        $('#coloresProveedor').selectpicker('refresh');

    }
});

function deleteMovimiento(fila, id) {
    const ordenDetalleNew = ordenDetalle.filter(orden => orden.idMaterial + orden.tipo + orden.idColor != id);
    ordenDetalle = ordenDetalleNew;
    console.log(ordenDetalle);
    tableOrden
        .row($(fila).parents('tr'))
        .remove()
        .draw();
    totales();

}

function precioU(id, wrote, cantidad) {
    var indexMaterial = ordenDetalle.map(orden => `${orden.idMaterial}-${orden.tipo}-${orden.idColor}`).indexOf(id);
    var sum = 0;
    console.log(indexMaterial);
    ordenDetalle[indexMaterial].precioU = +wrote;
    ordenDetalle[indexMaterial].montoCD = 0;
    $(`#monto-${id}`).val(0);
    $(`#porcentaje-${id}`).val(0);
    $(`#monto-${id}`).attr("disabled", false);
    $(`#porcentaje-${id}`).attr("disabled", false);
    $(`#subtotal-${id}`).text((parseFloat(wrote).toFixed(2) * parseInt(cantidad)).toFixed(2));
    totales();

}

function porcentaje(id, wrote) {
    var sum = 0;
    var indexMaterial = ordenDetalle.map(orden => `${orden.idMaterial}-${orden.tipo}-${orden.idColor}`).indexOf(id);
    if (wrote == "") {
        ordenDetalle[indexMaterial].montoCD = 0;
        $(`#monto-${id}`).val(0);
        $(`#subtotal-${id}`).text((ordenDetalle[indexMaterial].precioU * ordenDetalle[indexMaterial].cantidad).toFixed(2));
        return false
    }
    var monto = (ordenDetalle[indexMaterial].precioU * ordenDetalle[indexMaterial].cantidad / 100) * parseFloat(wrote)
    $(`#monto-${id}`).val(monto.toFixed(2));
    ordenDetalle[indexMaterial].montoCD = monto;
    $(`#subtotal-${id}`).text(((ordenDetalle[indexMaterial].precioU * ordenDetalle[indexMaterial].cantidad) + ordenDetalle[indexMaterial].montoCD).toFixed(2));
    totales();

}

function monto(id, wrote) {
    var sum = 0;
    var indexMaterial = ordenDetalle.map(orden => `${orden.idMaterial}-${orden.tipo}-${orden.idColor}`).indexOf(id);
    if (wrote == "") {
        ordenDetalle[indexMaterial].montoCD = 0;
        $(`#porcentaje-${id}`).val(0);
        $(`#subtotal-${id}`).text((ordenDetalle[indexMaterial].precioU * ordenDetalle[indexMaterial].cantidad).toFixed(2));
        return false
    }
    var porcentaje = (parseFloat(+wrote) / (ordenDetalle[indexMaterial].precioU * ordenDetalle[indexMaterial].cantidad)) * 100
    $(`#porcentaje-${id}`).val(porcentaje.toFixed(2));
    ordenDetalle[indexMaterial].montoCD = parseFloat(wrote);
    $(`#subtotal-${id}`).text(((ordenDetalle[indexMaterial].precioU * ordenDetalle[indexMaterial].cantidad) + ordenDetalle[indexMaterial].montoCD).toFixed(2));
    totales();

}

$("#enviarOrden").click(function (e) {
    e.preventDefault();
    $("#enviarOrden").attr('disabled', true);
    var ordenesPrecio = ordenDetalle.map(orden => orden.precioU);
    var lengthOrdenesPrecio = ordenesPrecio.filter(precio => precio === 0).length;
    var iva = $(`#selectIva`).val();
    if (lengthOrdenesPrecio > 0) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Todos los materiales deben de tener un precio!',
        }).then(result => {
            $("#enviarOrden").attr('disabled', false);
        })

    }
    else if (iva.trim() === '') {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El campo Iva es requerido!',
        }).then(result => {
            $("#enviarOrden").attr('disabled', false);
        })
    }
    else {
        $.ajax({
            type: "POST",
            url: "/postOrdenCompra",
            data: {
                _csrf: $('[name=_csrf]').val(),
                ordenCompraDetalle: JSON.stringify(ordenDetalle),
                idProveedor: $(`#proveedorSelect`).val(),
                iva: iva
            },
            success: function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'La orden se creo exitosamente',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    $(location).attr('href', '/orden-de-compra');
                })
            },
            error: function (response) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Error: ' + response,
                    timer: 1500
                }).then((result) => {
                    $(location).attr('href', '/orden-de-compra');
                })
            }
        });
    }

});

$(`#selectIva`).change(function (e) {
    e.preventDefault();
    totales();

});

function totales() {
    const iva = +$(`#selectIva`).val() / 100;
    const totalIva = ordenDetalle.reduce((acc, detalle) => acc + ((detalle.precioU * detalle.cantidad) + detalle.montoCD) * iva, 0);
    $(`#iva`).text('$' + totalIva.toFixed(2));
    const descuento = ordenDetalle.reduce((acc, detalle) => acc + detalle.montoCD, 0);
    $(`#descuento`).text('$' + descuento.toFixed(2));
    const subtotal = ordenDetalle.reduce((acc, detalle) => acc + detalle.precioU * detalle.cantidad, 0);
    $(`#subtotal`).text('$' + subtotal.toFixed(2));
    $(`#total`).text('$' + (subtotal + totalIva + descuento).toFixed(2));
}