$(document).ready(function () {

	listarLargo();
	listarProceso();
	listarMaquila();
	listarRuta();
	listarUbicaciones();

	// no borrar pues afectara el funcionamiento de rutas
	var table = $('.tablaProcesos')
		.DataTable({
			"ordering": false,
			"pageLength": 5,
			"responsive": true,
			"stateSave": true,
			"drawCallback": function () {
				$('.popoverxd').popover({
					container: 'body',
					trigger: 'hover'
				});
			},
			"columnDefs": [
				{ "className": "hidden", "searchable": false, "targets": 0 }
			],
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

});

$('#detalleTalla').on('shown.bs.modal', function () {
	$(document).off('focusin.modal');
});
function agregarLargo() {
	Swal.fire({
		title: 'Nuevo Largo',
		html:
			'<form class="row" autocomplete="off">' +
			'<div class="form-group col-sm-6">' +
			'<label for="nomenclatura">Nomenclatura</label>' +
			'<input type="text" class="form-control" id="nomenclatura" name="nomenclatura"  placeholder="XXL">' +
			'</div>' +
			'<div class="form-group col-sm-6">' +
			'<label for="descripcion">Descripci&oacute;n</label>' +
			'<input type="text" class="form-control" id="descripcionLargo" name="descripcionLargo"   placeholder="Extra largo">' +
			'</div>' +
			'</form>',
		inputAttributes: {
			autocapitalize: 'off'
		},
		showCancelButton: true,
		confirmButtonText: 'Agregar',
		cancelButtonText: 'Cancelar',
		showLoaderOnConfirm: true,
		preConfirm: (nomenclatura) => {
			if (document.getElementById("nomenclatura").value == "" || document.getElementById("descripcionLargo").value == "") {
				Swal.showValidationMessage(
					`Complete todos los campos`
				)
			}
		},
		allowOutsideClick: () => !Swal.isLoading()
	}).then((result) => {
		if (result.value && document.getElementById("nomenclatura").value && document.getElementById("descripcionLargo").value) {
			var nomenclatura = document.getElementById("nomenclatura").value;
			var descripcion = document.getElementById("descripcionLargo").value;
			$.ajax({
				type: "GET",
				url: "/verificar-duplicado-produccion",
				data: {
					'Lookup': nomenclatura,
					'Tipo': "Largo"


				}

			}).done(function (data) {
				if (data == false) {

					$.ajax({
						type: "POST",
						url: "/guardar-catalogo-produccion",
						data: {
							"_csrf": $('#token').val(),
							'nomenclatura': nomenclatura,
							'descripcion': descripcion

						}

					}).done(function (data) {
						listarLargo();
					});
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Insertado correctamente',
						showConfirmButton: false,
						timer: 1250
					})
					// / window.setTimeout(function(){location.reload()}, 2000);
				}// /fin segundoif
				else {
					Swal.fire({
						position: 'center',
						icon: 'error',
						title: 'registro duplicado no se ha insertado',
						showConfirmButton: false,
						timer: 1250
					})

				}
			});


		}
	});
}

function listarLargo() {

	$.ajax({
		method: "GET",
		url: "/listar-catalogo-produccion",
		data: {
			"Tipo": "Largo"
		},
		success: (data) => {
			$('#quitar2').remove();
			$('#contenedorTabla2').append("<div class='modal-body' id='quitar2'>" +
				"<table class='table table-striped table-bordered' id='idtable2' style='width:100%'>" +
				"<thead>" +
				"<tr>" +
				"<th>Clave</th>" +
				"<th>Nomenclatura</th>" +
				"<th>Descripción</th>" +

				"<th>Acciones</th>" +
				"</tr>" +
				"</thead>" +
				"</table>" + "</div>");
			var a;
			var b = [];

			for (i in data) {
				var creacion = data[i].actualizadoPor == null ? "" : data[i].actualizadoPor;
				var actulizacion = data[i].ultimaFechaModificacion == null ? "" : data[i].ultimaFechaModificacion;

				if (data[i].estatus == 1) {
					a = [
						"<tr>" +
						"<td>" + data[i].idText + "</td>",
						"<td>" + data[i].nombreLookup + "</td>",
						"<td>" + data[i].descripcionLookup + "</td>",
						"<td style='text-align: center'>" +
						"<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>" + data[i].creadoPor + " <br /><strong>Fecha de creación:</strong> " + data[i].fechaCreacion + "<br><strong>Modificado por:</strong>" + creacion + "<br><strong>Fecha de modicación:</strong>" + actulizacion + "'><i class='fas fa-info'></i></button> " +
						(rolEditar == 1 ? "<button onclick='editarLargo(this)' id='" + data[i].idLookup + "' nombre='" + data[i].nombreLookup + "' descripcion='" + data[i].descripcionLookup + "' class='btn btn-warning btn-circle btn-sm popoverxd edit_data_color' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button> " : " ") +
						(rolEliminar == 1 ? "<button onclick='bajarLargo(" + data[i].idLookup + ")' class='btn btn-danger btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>" : " ") +
						"</td>" +

						"<tr>"
					];
					b.push(a);

				}
				else {
					a = [
						"<tr>" +
						"<td>" + data[i].idText + "</td>",
						"<td>" + data[i].nombreLookup + "</td>",
						"<td>" + data[i].descripcionLookup + "</td>",
						"<td style='text-align: center'>" +
						"<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>" + data[i].creadoPor + " <br /><strong>Fecha de creación:</strong> " + data[i].fechaCreacion + "<br><strong>Modificado por:</strong>" + creacion + "<br><strong>Fecha de modicación:</strong>" + actulizacion + "'><i class='fas fa-info'></i></button> " +
						(rolEditar == 1 ? "<button onclick='editarLargo(this)' id='" + data[i].idLookup + "' nombre='" + data[i].nombreLookup + "' descripcion='" + data[i].descripcionLookup + "' class='btn btn-warning btn-circle btn-sm popoverxd edit_data_color' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button> " : " ") +
						(rolEliminar == 1 ? "<button onclick='subirLargo(" + data[i].idLookup + ")' class='btn btn-success btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-sort-up'></i></button>" : " ") +



						"</td>" +

						"<tr>"
					];
					b.push(a);

				}

			}


			var tablaColores = $('#idtable2').DataTable({
				"data": b,
				"ordering": true,
				"pageLength": 5,
				"drawCallback": function () {
					$('.popoverxd').popover({
						container: 'body',
						trigger: 'hover'
					});
				},
				"responsive": true,
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
			new $.fn.dataTable.FixedHeader(tablaColores);
		},
		error: (e) => {

		}
	})
}

function bajarLargo(id) {
	Swal.fire({
		title: '¿Deseas dar de baja el largo?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar'
	}).then((result) => {
		if (result.value && id != null) {

			$.ajax({
				type: "POST",
				url: "/baja-catalogo-produccion",
				data: {
					"_csrf": $('#token').val(),
					'id': id
				}

			}).done(function (data) {

				listarLargo();
			});
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Dado de baja correctamente',
				showConfirmButton: false,
				timer: 1250
			})
		}
	});

}
function subirLargo(id) {
	Swal.fire({
		title: '¿Deseas reactivar el largo?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar'
	}).then((result) => {
		if (result.value && id != null) {
			$.ajax({
				type: "POST",
				url: "/reactivar-catalogo-produccion",
				data: {
					"_csrf": $('#token').val(),
					'idcatalogo': id
				}

			}).done(function (data) {

				listarLargo();
			});
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Reactivado correctamente',
				showConfirmButton: false,
				timer: 1250
			})
		}
	});
}
function editarLargo(e) {

	Swal.fire({
		title: 'Editar largo',
		html:
			'<form class="row" autocomplete="off">' +
			'<div class="form-group col-sm-6">' +
			'<label for="nomenclatura">Nomenclatura</label>' +
			'<input type="text" class="form-control" value="' + e.getAttribute("nombre") + '" id="nomenclatura" name="nomenclatura"  placeholder="XXL">' +

			'<input type="hidden" class="form-control" value="' + e.getAttribute("id") + '" id="idLookup" name="idLookup" >' +
			'</div>' +
			'<div class="form-group col-sm-6">' +
			'<label for="descripcion">Descripci&oacute;n</label>' +
			'<input type="text" class="form-control" value="' + e.getAttribute("descripcion") + '"  id="descripcionLargo" name="descripcionLargo"   placeholder="Extra largo">' +
			'</div>' +
			'</form>',
		inputAttributes: {
			autocapitalize: 'off'
		},
		showCancelButton: true,
		confirmButtonText: 'Agregar',
		cancelButtonText: 'Cancelar',
		showLoaderOnConfirm: true,
		preConfirm: (nomenclatura) => {
			if (document.getElementById("nomenclatura").value == "" || document.getElementById("descripcionLargo").value == "") {
				Swal.showValidationMessage(
					`Complete todos los campos`
				)
			}
		},
		allowOutsideClick: () => !Swal.isLoading()
	}).then((result) => {
		if (result.value && document.getElementById("nomenclatura").value && document.getElementById("descripcionLargo").value) {
			var nomenclatura = document.getElementById("nomenclatura").value;
			var descripcion = document.getElementById("descripcionLargo").value;
			var idLookup = document.getElementById("idLookup").value;
			$.ajax({
				type: "GET",
				url: "/verificar-duplicado-produccion",
				data: {
					'Lookup': nomenclatura,
					'descripcion': descripcion,
					'Tipo': "Proceso"


				}

			}).done(function (data) {
				if (data == false) {

					$.ajax({
						type: "POST",
						url: "/editar-catalogo-produccion",
						data: {
							"_csrf": $('#token').val(),
							'largoNomenclatura': nomenclatura,
							'largoDescripcion': descripcion,
							'idLookup': idLookup

						}

					}).done(function (data) {
						listarLargo();
					});
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Actualizado correctamente',
						showConfirmButton: false,
						timer: 1250
					})
					// / window.setTimeout(function(){location.reload()}, 2000);
				}// /fin segundoif
				else {
					Swal.fire({
						position: 'center',
						icon: 'error',
						title: 'registro duplicado no se ha insertado',
						showConfirmButton: false,
						timer: 1250
					})

				}
			});


		}
	});
}

function listarProceso() {

	$.ajax({
		method: "GET",
		url: "/listar-catalogo-produccion",
		data: {
			"Tipo": "Proceso"
		},
		success: (data) => {

			var tabla = $('#tableProceso').DataTable();



			tabla.clear();

			$(data).each(function (i, v) { // indice, valor
				var fecha;
				var actualizo;
				if (v.actualizadoPor == null && v.ultimaFechaModificacion == null) {
					fecha = '';
					actualizo = '';
				} else {
					actualizo = v.actualizadoPor;
					fecha = v.ultimaFechaModificacion;
				}

				if (v.estatus == 1) {
					tabla.row.add([
						v.idText,
						v.nombreLookup,
						v.atributo1,
						v.descripcionLookup,
						(rolEditar == 1 ? '<button class="btn btn-secondary btn-circle btn-sm popoverxd" onclick="permisosProcesos(' + v.idLookup + ')" data-container="body" data-toggle="popover"  data-content="Permiso"> <i class="fas fa-user-plus"></i> </button>' : ""),
						'<button class="btn btn-info btn-circle btn-sm popoverxd" data-container="body" data-toggle="popover"  data-html="true" data-content="<strong>Creado por: </strong>' + v.creadoPor + ' <br /><strong>Fecha de creaci&oacute;n: </strong> ' + v.fechaCreacion + ' <br><strong>Modificado por: </strong>' + actualizo + '<br><strong>Fecha de modicaci&oacute;n: </strong>' + fecha + '"><i class="fas fa-info"></i></button>' +
						(rolEditar == 1 ? '<button class="btn btn-warning btn-circle btn-sm popoverxd" onclick="editProceso(this)" idLookup ="' + v.idLookup + '" atributo1 ="' + v.atributo1 + '"  nombre="' + v.nombreLookup + '" descripcion="' + v.descripcionLookup + '" data-container="body" data-toggle="popover"  data-content="Editar"><i class="fas fa-pen"></i></button>' : "") +
						(rolEliminar == 1 ? '<button class="btn btn-danger btn-circle btn-sm popoverxd" onclick="deleteProceso(' + v.idLookup + ')" data-container="body" data-toggle="popover"  data-content="Dar de baja"><i class="fas fa-caret-down"></i></button>' : "")

					]).node().id = "row";
				} else {
					tabla.row.add([
						v.idText,
						v.nombreLookup,
						v.atributo1,
						v.descripcionLookup,
						(rolEditar == 1 ? '<button class="btn btn-secondary btn-circle btn-sm popoverxd" onclick="permisosProcesos(' + v.idLookup + ')" data-container="body" data-toggle="popover"  data-content="Permiso"> <i class="fas fa-user-plus"></i> </button>' : ""),
						'<button class="btn btn-info btn-circle btn-sm popoverxd" data-container="body" data-toggle="popover"  data-html="true" data-content="<strong>Creado por: </strong>' + v.creadoPor + ' <br /><strong>Fecha de creaci&oacute;n: </strong> ' + v.fechaCreacion + ' <br><strong>Modificado por: </strong>' + actualizo + '<br><strong>Fecha de modicaci&oacute;n: </strong>' + fecha + '"><i class="fas fa-info"></i></button>' +
						(rolEditar == 1 ? '<button class="btn btn-warning btn-circle btn-sm popoverxd" onclick="editProceso(this)" idLookup ="' + v.idLookup + '" atributo1 ="' + v.atributo1 + '"  nombre="' + v.nombreLookup + '" descripcion="' + v.descripcionLookup + '" data-container="body" data-toggle="popover"  data-content="Editar"><i class="fas fa-pen"></i></button>' : "") +
						(rolEliminar == 1 ? '<button class="btn btn-success btn-circle btn-sm popoverxd" onclick="reactiveProceso(' + v.idLookup + ')" data-container="body" data-toggle="popover"  data-content="Reactivar"><i class="fas fa-caret-up"></i></button>' : "")

					]).node().id = "row";
				}

				tabla.draw(false);
				//fila = '<tr> <td>'+v[1]+'</td>  <td >'+ v[2] +'</td> <td >'+ v[3] +'</td> <td >'+ v[4] +'</td>  </tr>' ;



			})


		},
		error: (e) => {

		}
	})
}

//Reactivar pieza de trazo
$('#detalleProceso').on('shown.bs.modal', function () {
	$(document).off('focusin.modal');
});

function addProceso() {
	Swal.fire({
		title: 'Nuevo proceso',
		html:
			'<div class="row">' +
			'<div class="form-group col-sm-4">' +
			'<label for="descripcionProceso">Descripci&oacute;n</label>' +
			'<input type="text" class="form-control" id="descripcionProceso" placeholder="Trazo">' +
			'</div>' +
			'<div class="form-group col-sm-4">' +
			'<label for="tipoProceso">Tipo</label>' +
			'<select class="form-control" id="tipoProceso">' +
			'<option value="Agrupado">Agrupado</option>' +
			'<option value="Desagrupado">Desagrupado</option>' +
			'</select>' +
			'</div>' +
			'<div class="form-group col-sm-4">' +
			'<label for="origenProceso">Origen</label>' +
			'<select class="form-control" id="origenProceso">' +
			'<option value="Interno">Interno</option>' +
			'<option value="Externo">Externo</option>' +
			'</select>' +
			'</div>' +
			'</div>',
		inputAttributes: {
			autocapitalize: 'off'
		},
		showCancelButton: true,
		confirmButtonText: 'Agregar',
		cancelButtonText: 'Cancelar',
		showLoaderOnConfirm: true,
		preConfirm: (descripcionProceso) => {
			if (document.getElementById("descripcionProceso").value.length < 1) {
				Swal.showValidationMessage(
					`Complete todos los campos`
				)
			}
		},
		allowOutsideClick: () => !Swal.isLoading()
	}).then((result) => {
		if (result.value && document.getElementById("descripcionProceso").value && document.getElementById("origenProceso").value) {
			var descripcionProceso = document.getElementById("descripcionProceso").value;
			var origenProceso = document.getElementById("origenProceso").value;
			var tipoProceso = document.getElementById("tipoProceso").value;
			$.ajax({
				type: "GET",
				url: "/verificar-duplicado-produccion",
				data: {
					'Lookup': descripcionProceso,
					'descripcion': origenProceso,
					'Atributo1': tipoProceso,
					'Tipo': "Proceso"
				}

			}).done(function (data) {
				console.log("dataaa----->" + data)
				if (data == false) {

					$.ajax({
						type: "POST",
						url: "/guardar-catalogo-produccion",
						data: {
							"_csrf": $('#token').val(),
							'descripcionProceso': descripcionProceso,
							'origenProceso': origenProceso,
							'tipoProceso': tipoProceso,

						}

					}).done(function (data) {
						listarProceso();
					});
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Insertado correctamente',
						showConfirmButton: false,
						timer: 1250
					})
					// / window.setTimeout(function(){location.reload()}, 2000);
				}// /fin segundoif
				else {
					Swal.fire({
						position: 'center',
						icon: 'error',
						title: 'registro duplicado no se ha insertado',
						showConfirmButton: false,
						timer: 1250
					})

				}
			});


		}
	});
}
function editProceso(e) {

	Swal.fire({
		title: 'Editar proceso',
		html:
			'<div class="row">' +
			'<div class="form-group col-sm-4">' +
			'<label for="descripcionProceso">Descripci&oacute;n</label>' +
			'<input type="text" value="' + e.getAttribute("nombre") + '" class="form-control" id="descripcionProceso" placeholder="Trazo">' +
			'</div>' +
			'<div class="form-group col-sm-4">' +
			'<label for="tipoProceso">Tipo</label>' +
			'<select class="form-control" value="' + e.getAttribute("atributo1") + '" id="tipoProceso">' +
			'<option value="Agrupado">Agrupado</option>' +
			'<option value="Desagrupado">Desagrupado</option>' +
			'</select>' +
			'</div>' +
			'<div class="form-group col-sm-4">' +
			'<label for="origenProceso">Origen</label>' +
			'<select class="form-control" value="' + e.getAttribute("descripcion") + '" id="origenProceso">' +
			'<option value="Interno">Interno</option>' +
			'<option value ="Externo">Externo</option>' +
			'</select>' +
			'<input type="hidden" value=" ' + e.getAttribute("idlookup") + ' " class="swal2-input" id="idlookup" placeholder="Pantalón">' +
			'</div>' +
			'</div>',
		inputAttributes: {
			autocapitalize: 'off'
		},
		showCancelButton: true,
		confirmButtonText: 'Agregar',
		cancelButtonText: 'Cancelar',
		showLoaderOnConfirm: true,
		preConfirm: (descripcionProceso) => {
			if (document.getElementById("descripcionProceso").value.length < 1) {
				Swal.showValidationMessage(
					`Complete todos los campos`
				)
			}
		},
		allowOutsideClick: () => !Swal.isLoading()
	}).then((result) => {
		if (result.value && document.getElementById("descripcionProceso").value && document.getElementById("origenProceso").value) {
			var descripcionProceso = document.getElementById("descripcionProceso").value;
			var origenProceso = document.getElementById("origenProceso").value;
			var idLookup = document.getElementById("idlookup").value;
			var tipoProceso = document.getElementById("tipoProceso").value;
			$.ajax({
				type: "GET",
				url: "/verificar-duplicado-produccion",
				data: {
					'Lookup': descripcionProceso,
					'descripcion': origenProceso,
					'Atributo1': tipoProceso,
					'Tipo': "Proceso"
				}
			}).done(function (data) {
				console.log("dataaa----->" + data)
				if (data == false) {

					$.ajax({
						type: "POST",
						url: "/editar-catalogo-produccion",
						data: {
							"_csrf": $('#token').val(),
							'descripcionProceso': descripcionProceso,
							'origenProceso': origenProceso,
							'idLookup': idLookup,
							'tipoProceso': tipoProceso,

						}

					}).done(function (data) {
						listarProceso();
					});
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Actualizado correctamente',
						showConfirmButton: false,
						timer: 1250
					})
					// / window.setTimeout(function(){location.reload()}, 2000);
				}// /fin segundoif
				else {
					Swal.fire({
						position: 'center',
						icon: 'error',
						title: 'registro duplicado no se ha insertado',
						showConfirmButton: false,
						timer: 1250
					})

				}
			});


		}
	});
}

function deleteProceso(id) {
	console.log(id)
	Swal.fire({
		title: '¿Deseas dar de baja el proceso?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar'
	}).then((result) => {
		if (result.value && id != null) {

			$.ajax({
				type: "POST",
				url: "/baja-catalogo-produccion",
				data: {
					"_csrf": $('#token').val(),
					'id': id
				}

			}).done(function (data) {

				listarProceso();
			});
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Dado de baja correctamente',
				showConfirmButton: false,
				timer: 1250
			})
		}
	});
}
function reactiveProceso(id) {
	Swal.fire({
		title: '¿Deseas reactivar el proceso?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar'
	}).then((result) => {
		if (result.value && id != null) {
			$.ajax({
				type: "POST",
				url: "/reactivar-catalogo-produccion",
				data: {
					"_csrf": $('#token').val(),
					'idcatalogo': id
				}

			}).done(function (data) {

				listarProceso();
			});
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Reactivado correctamente',
				showConfirmButton: false,
				timer: 1250
			})
		}
	});
}

//Reactivar pieza de trazo
$('#detalleRuta').on('shown.bs.modal', function () {
	$(document).off('focusin.modal');
});


function listarRuta() {

	$.ajax({
		method: "GET",
		url: "/listar-catalogo-produccion",
		data: {
			"Tipo": "Ruta"
		},
		success: (data) => {

			var tabla = $('#tableRuta').DataTable();



			tabla.clear();

			$(data).each(function (i, v) { // indice, valor
				var fecha;
				var actualizo;
				if (v.actualizadoPor == null && v.ultimaFechaModificacion == null) {
					fecha = '';
					actualizo = '';
				} else {
					actualizo = v.actualizadoPor;
					fecha = v.ultimaFechaModificacion;
				}

				if (v.estatus == 1) {
					tabla.row.add([
						v.idText,
						v.nombreLookup,
						'<td class="text-center">' +
						'<button onclick="verProcesosRuta(' + v.idLookup + ')" class="btn btn-primary btn-circle btn-sm popoverxd" id="modalDetalles" data-container="body" data-toggle="modal" data-target="#infoProcesos"  data-content="Ver procesos"><i class="fas fa-cog"></i></button>' +
						'</td>',
						'<button class="btn btn-info btn-circle btn-sm popoverxd" data-container="body" data-toggle="popover"  data-html="true" data-content="<strong>Creado por: </strong>' + v.creadoPor + ' <br /><strong>Fecha de creaci&oacute;n: </strong> ' + v.fechaCreacion + ' <br><strong>Modificado por: </strong>' + actualizo + '<br><strong>Fecha de modicaci&oacute;n: </strong>' + fecha + '"><i class="fas fa-info"></i></button>' +
						(rolEditar == 1 ? '<button class="btn btn-warning btn-circle btn-sm popoverxd" onclick="editarRuta(this)" idLookup ="' + v.idLookup + '"  nombre="' + v.nombreLookup + '" descripcion="' + v.descripcionLookup + '" data-container="body" data-toggle="popover"  data-content="Editar"><i class="fas fa-pen"></i></button>' : " ") +
						(rolEliminar == 1 ? '<button class="btn btn-danger btn-circle btn-sm popoverxd" onclick="deleteRuta(' + v.idLookup + ')" data-container="body" data-toggle="popover"  data-content="Dar de baja"><i class="fas fa-caret-down"></i></button>' : " ")

					]).node().id = "row";
				} else {
					tabla.row.add([
						v.idText,
						v.nombreLookup,
						'<td class="text-center">' +
						'<button onclick="verProcesosRuta(' + v.idLookup + ')" class="btn btn-primary btn-circle btn-sm popoverxd" id="modalDetalles" data-container="body" data-toggle="modal" data-target="#infoProcesos"  data-content="Ver procesos"><i class="fas fa-cog"></i></button>' +
						'</td>',

						'<button class="btn btn-info btn-circle btn-sm popoverxd" data-container="body" data-toggle="popover"  data-html="true" data-content="<strong>Creado por: </strong>' + v.creadoPor + ' <br /><strong>Fecha de creaci&oacute;n: </strong> ' + v.fechaCreacion + ' <br><strong>Modificado por: </strong>' + actualizo + '<br><strong>Fecha de modicaci&oacute;n: </strong>' + fecha + '"><i class="fas fa-info"></i></button>' +
						(rolEditar == 1 ? '<button class="btn btn-warning btn-circle btn-sm popoverxd" onclick="editarRuta(this)" idLookup ="' + v.idLookup + '"  nombre="' + v.nombreLookup + '" descripcion="' + v.descripcionLookup + '" data-container="body" data-toggle="popover"  data-content="Editar"><i class="fas fa-pen"></i></button>' : " ") +
						(rolEliminar == 1 ? '<button class="btn btn-success btn-circle btn-sm popoverxd" onclick="reactiveRuta(' + v.idLookup + ')" data-container="body" data-toggle="popover"  data-content="Reactivar"><i class="fas fa-caret-up"></i></button>' : " ")

					]).node().id = "row";
				}

				tabla.draw(false);
				//fila = '<tr> <td>'+v[1]+'</td>  <td >'+ v[2] +'</td> <td >'+ v[3] +'</td> <td >'+ v[4] +'</td>  </tr>' ;



			})


		},
		error: (e) => {

		}
	})
}
//listar_rutas_procesos
function verProcesosRuta(id) {
	$.ajax({
		method: "GET",
		url: "/listar_rutas_procesos",
		data: {
			"id": id
		},
		success: (data) => {
			var tabla = $('#tableListaProcesos').DataTable();
			var rows = tabla
				.rows()
				.remove()
				.draw();
			for (i in data) {
				tabla.row.add([data[i][1]]).node().id = "row";
				tabla.draw(false);
			}
		},
		error: (e) => {
		}
	})
}
function editarRuta(e) {
	SelectListarProcesos();
	$.ajax({
		method: "GET",
		url: "/listar_rutas_procesos",
		data: {
			"id": e.getAttribute("idLookup")
		},
		success: (data) => {
			var tabla = $('#tablaProcesos').DataTable();
			var rows = tabla
				.rows()
				.remove()
				.draw();
			for (i in data) {
				tabla.row.add([
					data[i][3],
					data[i][2],
					data[i][1],
					'<td><button type="button" onclick="eliminardeBase(' + data[i][0] + ', this)" class="btn btn-sm icon-btn btn-danger text-white btn_remove"><span class="btn-glyphicon spancircle fas fa-times fa-lg img-circle text-danger"></span>Eliminar</button></td>'

				]).node().id = "row";
				tabla.draw(false);
			}
		},
		error: (e) => {
		}
	})
	console.log(e.getAttribute("nombre"))
	$('#nombreRuta').val(e.getAttribute("nombre"));
	$('#idLookupRuta').val(e.getAttribute("idLookup"));
	$('#botonGuardarRuta').attr('onclick', 'editar_ruta_proceso()');
	$('#addRuta').modal('show');



}
function SelectListarProcesos() {
	$.ajax({
		method: "GET",
		url: "/listar-catalogo-produccion-procesos",
		data: {
			"Tipo": "Proceso"
		},
		success: (data) => {
			$('#proceso').empty();

			for (i in data) {
				$('#proceso').append("<option value=" + data[i].idLookup + " text=" + data[i].nombreLookup + " tipo=" + data[i].descripcionLookup + " >" + data[i].nombreLookup + "</option>");
			}
			$('#proceso').selectpicker('refresh');

			$("#boton-add-proceso").prop("disabled", false);



		},
		error: (e) => {

		}
	})
}
function addRuta() {
	SelectListarProcesos();
	$('#addRuta').modal('show');
	var table = $('#tablaProcesos').DataTable();
	var rows = table
		.rows()
		.remove()
		.draw();
	$('#botonGuardarRuta').attr('onclick', 'guardarRuta()');
	$('#nombreRuta').val(null);

}
function agregarProceso() {
	var t = $('#tablaProcesos').DataTable();
	var repetido = false;
	console.log(document.getElementById("proceso").value == null)


	if (document.getElementById("proceso").value != 0) {
		$('#tablaProcesos tr').each(function () {

			if ($(this).find('td').eq(0).html() == $("#proceso option:selected").attr("value")) {
				Swal.fire({
					position: 'center',
					icon: 'error',
					title: 'Ya ha seleccionado ese proceso',
					showConfirmButton: true
				});
				repetido = true;
			}


		});

		if (repetido != true) {


			t.row.add([
				'<td style="display:none;" >' + $("#proceso option:selected").attr("value") + '</td>',
				'<td>' + $("#proceso option:selected").attr("tipo") + '</td>',

				'<td>' + $("#proceso option:selected").attr("text") + '</td>',
				'<td><button type="button" onclick="eliminar(this)" class="btn btn-sm icon-btn btn-danger text-white btn_remove"><span class="btn-glyphicon spancircle fas fa-times fa-lg img-circle text-danger"></span>Eliminar</button></td>'


			]).draw(false);
			$('#proceso').val(null);

			$('#proceso').selectpicker('refresh');

		}


	}
	else {
		Swal.fire({
			position: 'center',
			icon: 'error',
			title: 'Seleccione un proceso',
			showConfirmButton: true
		});
	}
}

function guardarRuta() {
	var datos = [];
	$('#tablaProcesos tr').each(function () {
		if ($(this).find('td').eq(1).html() != null) {
			datos.push({
				'id_proceso': $(this).find('td').eq(0).html(),
			});
		}
	});

	if ($('#nombreRuta').val() == null || $('#nombreRuta').val() == "") {
		Swal.fire({
			position: 'center',
			icon: 'error',
			title: 'Ingrese un nombre de ruta, por favor',
			showConfirmButton: true
		});
	}
	else if ($.isEmptyObject(datos)) {
		Swal.fire({
			position: 'center',
			icon: 'error',
			title: 'Ingrese al menos un proceso, por favor',
			showConfirmButton: true
		});
	}
	else {
		$.ajax({
			type: "GET",
			url: "/verificar-duplicado-produccion",
			data: {
				'Lookup': $('#nombreRuta').val(),
				'Tipo': "Ruta"


			}

		}).done(function (data) {
			console.log("dataaa----->" + data)
			if (data == false) {

				$.ajax({
					type: "POST",
					url: "/guardar-catalogo-produccion",
					data: {
						datos: JSON.stringify(datos),
						'ruta': $('#nombreRuta').val(),
						"_csrf": $('#token').val()
					},
					beforeSend: function () {

					},

					success: function (data) {
						listarRuta();
						$("#addRuta").modal("hide");
						Swal.fire({
							position: 'center',
							icon: 'success',
							title: 'Agregado correctamente',
							showConfirmButton: true,
							onBeforeOpen: () => {

							},
						});
					}
				})
				// / window.setTimeout(function(){location.reload()}, 2000);
			}// /fin segundoif
			else {
				Swal.fire({
					position: 'center',
					icon: 'error',
					title: 'registro duplicado no se ha insertado',
					showConfirmButton: false,
					timer: 1250
				})

			}
		});
	}
}

function editar_ruta_proceso() {

	var datos = [];
	$('#tablaProcesos tr').each(function () {
		if ($(this).find('td').eq(1).html() != null) {
			datos.push({
				'id_proceso': $(this).find('td').eq(0).html(),
			});
		}
	});

	if ($('#nombreRuta').val() == null || $('#nombreRuta').val() == "") {
		Swal.fire({
			position: 'center',
			icon: 'error',
			title: 'Ingrese un nombre de ruta, por favor',
			showConfirmButton: true
		});
	}
	else if ($.isEmptyObject(datos)) {
		Swal.fire({
			position: 'center',
			icon: 'error',
			title: 'Ingrese al menos un proceso, por favor',
			showConfirmButton: true
		});
	}
	else {
		$.ajax({
			type: "GET",
			url: "/validar-ruta-editar",
			data: {
				'idLookup': $('#idLookupRuta').val(),
				'nombre': $('#nombreRuta').val()


			}

		}).done(function (data) {
			console.log("dataaa----->" + data)
			if (data == false) {

				$.ajax({
					type: "POST",
					url: "/edita-ruta-produccion",
					data: {
						datos: JSON.stringify(datos),
						'nombre': $('#nombreRuta').val(),
						'idLookup': $('#idLookupRuta').val(),
						"_csrf": $('#token').val()
					},
					beforeSend: function () {

					},

					success: function (data) {
						listarRuta();
						$("#addRuta").modal("hide");
						Swal.fire({
							position: 'center',
							icon: 'success',
							title: 'Editado correctamente',
							showConfirmButton: true,
							onBeforeOpen: () => {

							},
						});
					}
				})
				// / window.setTimeout(function(){location.reload()}, 2000);
			}// /fin segundoif
			else {
				Swal.fire({
					position: 'center',
					icon: 'error',
					title: 'registro duplicado no se ha insertado',
					showConfirmButton: true,
					timer: 1250
				})

			}
		});
	}

}

function eliminar(t) {
	var tabla = $('#tablaProcesos').DataTable();
	var td = t.parentNode;
	var tr = td.parentNode;
	var table = tr.parentNode;
	tabla.row(tr).remove().draw(false);
}
function eliminardeBase(id, t) {
	$.ajax({
		data: { 'id': id },
		url: '/elimiar_MN_ruta',
		type: 'GET',

		success: function (data) {
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Eliminado correctamente',
				showConfirmButton: true

			});
		},
		complete: function () {
			var tabla = $('#tablaProcesos').DataTable();
			var td = t.parentNode;
			var tr = td.parentNode;
			var table = tr.parentNode;
			tabla.row(tr).remove().draw(false);

		},
	})

}


function deleteRuta(id) {

	Swal.fire({
		title: '¿Deseas dar de baja la ruta?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar'
	}).then((result) => {
		if (result.value && id != null) {

			$.ajax({
				type: "POST",
				url: "/baja-catalogo-produccion",
				data: {
					"_csrf": $('#token').val(),
					'id': id
				}

			}).done(function (data) {

				listarRuta();
			});
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Dado de baja correctamente',
				showConfirmButton: false,
				timer: 1250
			})
		}
	});
}
function reactiveRuta(id) {

	Swal.fire({
		title: '¿Deseas reactivar la ruta?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar'
	}).then((result) => {
		if (result.value && id != null) {
			$.ajax({
				type: "POST",
				url: "/reactivar-catalogo-produccion",
				data: {
					"_csrf": $('#token').val(),
					'idcatalogo': id
				}

			}).done(function (data) {

				listarRuta();
			});
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Reactivado correctamente',
				showConfirmButton: false,
				timer: 1250
			})
		}
	});
}


// ALMACEN LOGICO
$('#detalleEntrega').on('shown.bs.modal', function () {
	$(document).off('focusin.modal');
});
function agregarEntrega() {
	Swal.fire({
		title: 'Nueva falla de entrega',
		html:
			'<div class="row">' +
			'<div class="form-group col-md-12">' +
			'<label for="fallaEntrega">Falla de entrega</label>' +
			'<input type="text" class="form-control" id="fallaEntrega" placeholder="Especificar">' +
			'</div>' +
			'</div>',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.value) {
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Falla de entrega agregada correctamente',
				showConfirmButton: false,
				timer: 2500
			})
		}
	});
}
function editarEntrega() {
	Swal.fire({
		title: 'Editar falla de entrega',
		html:
			'<div class="row">' +
			'<div class="form-group col-md-12">' +
			'<label for="fallaEntregaE">Falla de entrega</label>' +
			'<input type="text" class="form-control" id="fallaEntregaE" placeholder="Especificar">' +
			'</div>' +
			'</div>',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.value) {
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Falla de entrega editada correctamente',
				showConfirmButton: false,
				timer: 2500
			})
		}
	});
}
function bajaEntrega() {
	Swal.fire({
		title: '¿Deseas dar de baja la falla de entrega?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.value) {
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Falla de entrega dada de baja correctamente',
				showConfirmButton: false,
				timer: 2500
			})
		}
	});
}
function altaEntrega() {
	Swal.fire({
		title: '¿Deseas dar de alta la falla de entrega?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.value) {
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Falla de entrega dada de alta correctamente',
				showConfirmButton: false,
				timer: 2500
			})
		}
	});
}

$('#detalleUbicacion').on('shown.bs.modal', function () {
	$(document).off('focusin.modal');
});
function addUbicacion() {

	Swal.fire({
		title: 'Nueva ubicaci&oacute;n',
		html:
			'<div class="row">' +
			'<div class="form-group col-md-12">' +
			'<label for="nombreUbicacion">Nombre</label>' +
			'<input type="text" class="form-control" id="nombreUbicacion" name="nombreUbicacion" placeholder="Ubicación">' +
			'</div>' +
			'</div>',
		showCancelButton: true,
		cancelButtonColor: '#dc3545',
		cancelButtonText: 'Cancelar',
		confirmButtonText: 'Agregar',
		confirmButtonColor: '#0288d1',
		preConfirm: (nombreUbicacion) => {
			if (document.getElementById("nombreUbicacion").value.length < 1) {

				Swal.showValidationMessage(
					`Complete todos los campos`
				)
			}
		},
	}).then((result) => {

		if (result.value && document.getElementById("nombreUbicacion").value) {
			var nombreUbicacion = document.getElementById("nombreUbicacion").value;
			$.ajax({
				type: "GET",
				url: "/verificar-duplicado-produccion",
				data: {
					'Lookup': nombreUbicacion,
					'Tipo': "Ubicación"


				}

			}).done(function (data) {
				if (data == false) {

					$.ajax({
						type: "POST",
						url: "/guardar-catalogo-produccion",
						data: {
							"_csrf": $('#token').val(),
							'nombreUbicacion': nombreUbicacion,

						}

					}).done(function (data) {
						listarUbicaciones();
					});
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Insertado correctamente',
						showConfirmButton: false,
						timer: 1250
					})
					// / window.setTimeout(function(){location.reload()}, 2000);
				}// /fin segundoif
				else {
					Swal.fire({
						position: 'center',
						icon: 'error',
						title: 'registro duplicado no se ha insertado',
						showConfirmButton: false,
						timer: 1250
					})

				}
			});


		}

	});


	$.ajax({
		method: "GET",
		url: "/listar_encargados_ubicaciones",
		data: {},
		success: (data) => {
			$('#selectResponsableUbicacion').append('<option value="">Seleccione uno...</option>');
			$.each(data, function (key, val) {
				$('#selectResponsableUbicacion').append('<option value="' + val[0] + '">' + val[1] + '</option>');
			})
			//$('.selectpicker').selectpicker(["refresh"]);
		},
		error: (e) => {

		}
	})

}
function listarUbicaciones() {
	$.ajax({
		method: "GET",
		url: "/listar_ubicaciones",
		data: {},
		success: (data) => {
			var tabla = $('#tableUbicacion').DataTable();
			tabla.clear();
			$(data).each(function (i, v) { // indice, valor
				var fecha;
				var actualizo;
				if (v[7] == null && v[8] == null) {
					fecha = '';
					actualizo = '';
				} else {
					actualizo = v[6];
					fecha = v[8];
				}
				if (v[4] == 1) {
					tabla.row.add([
						v[1],
						v[2],
						'<button class="btn btn-info btn-circle btn-sm popoverxd" data-container="body" data-toggle="popover"  data-html="true" data-content="<strong>Creado por: </strong>' + v[5] + ' <br /><strong>Fecha de creaci&oacute;n: </strong> ' + v[6] + ' <br><strong>Modificado por: </strong>' + actualizo + '<br><strong>Fecha de modicaci&oacute;n: </strong>' + fecha + '"><i class="fas fa-info"></i></button>' +
						(rolEditar == 1 ? '<button class="btn btn-warning btn-circle btn-sm popoverxd" onclick="editarUbicacion(this)" idLookup ="' + v[0] + '"  nombre="' + v[2] + '" empleado="' + v[9] + '" data-container="body" data-toggle="popover"  data-content="Editar"><i class="fas fa-pen"></i></button>' : "") +
						(rolEliminar == 1 ? '<button class="btn btn-danger btn-circle btn-sm popoverxd" onclick="deleteUbicacion(' + v[0] + ')" data-container="body" data-toggle="popover"  data-content="Dar de baja"><i class="fas fa-caret-down"></i></button>' : "")

					]).node().id = "row";
				} else {
					tabla.row.add([
						v[1],
						v[2],
						'<button class="btn btn-info btn-circle btn-sm popoverxd" data-container="body" data-toggle="popover"  data-html="true" data-content="<strong>Creado por: </strong>' + v[5] + ' <br /><strong>Fecha de creaci&oacute;n: </strong> ' + v[6] + ' <br><strong>Modificado por: </strong>' + actualizo + '<br><strong>Fecha de modicaci&oacute;n: </strong>' + fecha + '"><i class="fas fa-info"></i></button>' +
						(rolEditar == 1 ? '<button class="btn btn-warning btn-circle btn-sm popoverxd" onclick="editProceso(this)" idLookup ="' + v[1] + '"  nombre="' + v[1] + '" descripcion="' + v[1] + '" data-container="body" data-toggle="popover"  data-content="Editar"><i class="fas fa-pen"></i></button>' : "") +
						(rolEliminar == 1 ? '<button class="btn btn-success btn-circle btn-sm popoverxd" onclick="reactiveUbicacion(' + v[0] + ')" data-container="body" data-toggle="popover"  data-content="Reactivar"><i class="fas fa-caret-up"></i></button>' : "")

					]).node().id = "row";
				}
				tabla.draw(false);
				//fila = '<tr> <td>'+v[1]+'</td>  <td >'+ v[2] +'</td> <td >'+ v[3] +'</td> <td >'+ v[4] +'</td>  </tr>' 
			})
		},
		error: (e) => {

		}
	})
}

function deleteUbicacion(id) {
	console.log(id)
	Swal.fire({
		title: '¿Deseas dar de baja la ubicación?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar'
	}).then((result) => {
		if (result.value && id != null) {

			$.ajax({
				type: "POST",
				url: "/baja-catalogo-produccion",
				data: {
					"_csrf": $('#token').val(),
					'id': id
				}

			}).done(function (data) {


				listarUbicaciones();
			});
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Dado de baja correctamente',
				showConfirmButton: false,
				timer: 1250
			})
		}
	});

}
function reactiveUbicacion(id) {
	Swal.fire({
		title: '¿Deseas reactivar la ubicación?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar'
	}).then((result) => {
		if (result.value && id != null) {
			$.ajax({
				type: "POST",
				url: "/reactivar-catalogo-produccion",
				data: {
					"_csrf": $('#token').val(),
					'idcatalogo': id
				}

			}).done(function (data) {

				listarUbicaciones();
			});
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Reactivado correctamente',
				showConfirmButton: false,
				timer: 1250
			})
		}
	});


}
function editarUbicacion(e) {
	Swal.fire({
		title: 'Editar ubicación',
		html:

			'<div class="row">' +
			'<div class="form-group col-md-6">' +
			'<label for="nombreUbicacion">Nombre</label>' +
			'<input type="text" class="form-control" value=" ' + e.getAttribute("nombre") + ' " id="nombreUbicacion" name="nombreUbicacion"  placeholder="Ubicación">' +
			'</div>' +
			'<input type="hidden" value=" ' + e.getAttribute("idlookup") + ' " id="idlookup" name="idlookup" >' +
			'<div class="form-group col-md-6">' +
			'<label for="selectResponsableUbicacion">Responsable</label>' +
			'<select id="selectResponsableUbicacion" disabled name="selectResponsableUbicacion"  class="form-control" >' +


			'</select>' +
			'</div>' +


			'</div>',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Confirmar',
		cancelButtonText: 'Cancelar',
		preConfirm: (nombreUbicacion) => {
			if (document.getElementById("nombreUbicacion").value.length < 1 || document.getElementById("selectResponsableUbicacion").value.length == "") {

				Swal.showValidationMessage(
					`Complete todos los campos`
				)
			}
		},
	}).then((result) => {

		if (result.value && document.getElementById("nombreUbicacion").value && document.getElementById("selectResponsableUbicacion").value) {
			var nombreUbicacion = document.getElementById("nombreUbicacion").value;
			var responsable = document.getElementById("selectResponsableUbicacion").value;
			var idLookup = document.getElementById("idlookup").value;;
			$.ajax({
				type: "GET",
				url: "/verificar-duplicado-produccion",
				data: {
					'Lookup': nombreUbicacion,
					'descripcion': responsable,
					'Tipo': "Ubicación"


				}

			}).done(function (data) {
				console.log("dataaa----->" + data)
				if (data == false) {

					$.ajax({
						type: "POST",
						url: "/editar-catalogo-produccion",
						data: {
							"_csrf": $('#token').val(),
							'nombreUbicacion': nombreUbicacion,
							'responsablesUbicacion': responsable,
							'idLookup': idLookup

						}

					}).done(function (data) {
						listarUbicaciones();
					});
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Actualizado correctamente',
						showConfirmButton: false,
						timer: 1250
					})
					// / window.setTimeout(function(){location.reload()}, 2000);
				}// /fin segundoif
				else {
					Swal.fire({
						position: 'center',
						icon: 'error',
						title: 'registro duplicado no se ha insertado',
						showConfirmButton: false,
						timer: 1250
					})

				}
			});


		}

	});

	$.ajax({
		method: "GET",
		url: "/listar_encargados_ubicaciones",
		data: {},
		success: (data) => {
			$.each(data, function (key, val) {
				if (val[0] == e.getAttribute("empleado")) {
					$('#selectResponsableUbicacion').append('<option selected value="' + val[0] + '">' + val[1] + '</option>');
				} else {
					$('#selectResponsableUbicacion').append('<option value="' + val[0] + '">' + val[1] + '</option>');
				}

			})
			document.getElementById("selectResponsableUbicacion").disabled = false;
			//$('.selectpicker').selectpicker(["refresh"]);
		},
		error: (e) => {

		}
	})
}

function llenarSelectUsuariosProceso(id) {
	$.ajax({
		method: "GET",
		url: "/listar_usuarios_disponibles_by_proceso",
		data: { 'idProceso': $('#idLookupProceso').val() },
		success: (data) => {
			$("#selectUser").empty();
			$(data).each(function (i, v) {

				$('#selectUser').append('<option  value="' + v[0] + '">' + v[1] + '</option>');


			})
			$('#selectUser').selectpicker('refresh');

		}, complete: function () {
			$("#boton-add-proceso-permiso").prop('disabled', false);

			$('#selectUser').val(id);
			$('#selectUser').selectpicker('refresh');

		},
		error: (e) => {

		}
	})
}


function llenarTablatUsuariosProceso(id) {
	var tabla = $('#tablaProcesosPermisos').DataTable();
	tabla.rows().remove().draw();
	$.ajax({
		method: "GET",
		url: "/listar_usuarios_asignados_by_proceso",
		data: { 'idProceso': id },
		success: (data) => {

			$(data).each(function (i, v) {

				tabla.row.add([
					v[1],
					'<td><button type="button" onclick="eliminarPermiso(' + v[0] + ')" class="btn btn-sm icon-btn btn-danger text-white btn_remove"><span class="btn-glyphicon spancircle fas fa-times fa-lg img-circle text-danger"></span>Eliminar</button></td>'

				]).node().id = "row";


				tabla.draw(false);
				//fila = '<tr> <td>'+v[1]+'</td>  <td >'+ v[2] +'</td> <td >'+ v[3] +'</td> <td >'+ v[4] +'</td>  </tr>' ;



			})


		},
		error: (e) => {

		}
	})
}
function permisosProcesos(id) {
	$("#idLookupProceso").val(id);
	llenarSelectUsuariosProceso(null);
	llenarTablatUsuariosProceso(id);
	$("#modalPermisosProcesos").modal("show");

}
function agregarProcesoPermiso() {

	if ($("#selectUser").val() == null || $("#selectUser").val() == "" || $("#idLookupProceso").val() == "") {
		Swal.fire({
			position: 'center',
			icon: 'warning',
			title: 'Complete el formulario!',
			showConfirmButton: true
		});
	} else {

		$("#boton-add-proceso-permiso").prop('disabled', true);
		$.ajax({
			method: "GET",
			url: "/guardar_usuario_proceso",
			data: { 'idProceso': $("#idLookupProceso").val(), 'idUsuario': $("#selectUser").val() },
			success: (data) => {
				if (data == true) {
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Insertado correctamente!',
						showConfirmButton: true
					});
					llenarTablatUsuariosProceso($("#idLookupProceso").val());
					llenarSelectUsuariosProceso($("#idLookupProceso").val());
				}
			},
			error: (e) => {

			}
		})


	}
}

function eliminarPermiso(id) {


	Swal.fire({
		title: '¿Deseas eliminar este usuario?',
		icon: 'question',
		showCancelButton: true,
		cancelButtonColor: '#dc3545',
		cancelButtonText: 'Cancelar',
		confirmButtonText: 'Eliminar',
		confirmButtonColor: '#28A745',
	}).then((result) => {
		if (result.value) {
			$.ajax({
				method: "GET",
				url: "/eliminar_usuario_proceso",
				data: { 'idPermiso': id },
				beforeSend: function () {
				},
				success: (data) => {
					Swal.fire({
						position: 'center',
						icon: 'success',
						title: 'Eliminado correctamente!',
						showConfirmButton: true
					});
					if (data == true) {
						llenarTablatUsuariosProceso($("#idLookupProceso").val());
						llenarSelectUsuariosProceso($("#idLookupProceso").val());
					}

				},
				error: (data) => {

				}
			});


		}
	})



}
