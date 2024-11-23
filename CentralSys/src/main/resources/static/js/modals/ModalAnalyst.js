document.addEventListener("DOMContentLoaded", function () {
    // Obtener el formulario y el botón de aprobación
    var form = document.querySelector("form");
    var approveButton = document.getElementById("approveButton");
    var rejectButton = document.getElementById("rejectButton");

    // Agregar un controlador de clic al botón de aprobación
    approveButton.addEventListener("click", function (event) {
        event.preventDefault(); // Evitar que el formulario se envíe automáticamente
        form.action="/approve-credit-analyst"
        // Mostrar la alerta con un indicador de carga al inicio del envío del formulario
        Swal.fire({
            position: 'center',
            icon: 'info',
            title: 'sending response',
            timerProgressBar: true,
            showConfirmButton: false,
            timer: 2500
        }).then((result) => {
            /* Read more about handling dismissals below */
            if (result.dismiss === Swal.DismissReason.timer) {
                Swal.fire({
                    position: 'center',
                    icon: 'success',
                    title: 'successful sending!',
                    showConfirmButton: false,
                })
            }
        })
        // Enviar el formulario
        form.submit();
    });


    // Agregar un controlador de clic al botón de aprobación
    rejectButton.addEventListener("click", function (event) {
        event.preventDefault(); // Evitar que el formulario se envíe automáticamente
        form.action="/reject-credit-analyst"

        // Mostrar la alerta con un indicador de carga al inicio del envío del formulario
        Swal.fire({
            position: 'center',
            icon: 'info',
            title: 'sending response',
            timerProgressBar: true,
            showConfirmButton: false,
            timer: 2500
        }).then((result) => {
            /* Read more about handling dismissals below */
            if (result.dismiss === Swal.DismissReason.timer) {
                Swal.fire({
                    position: 'center',
                    icon: 'success',
                    title: 'successful sending!',
                    showConfirmButton: false,
                })
            }
        })
        // Enviar el formulario
        form.submit();
    });
});