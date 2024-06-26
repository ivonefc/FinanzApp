document.addEventListener('DOMContentLoaded', function() {
    // Funcionalidad para mostrar/ocultar detalles de compartir movimiento
    document.querySelectorAll('input[name="compartirMovimiento"]').forEach((elem) => {
        elem.addEventListener('change', (event) => {
            console.log('Cambio detectado en compartirMovimiento');
            document.getElementById('compartirDetalles').style.display = event.target.value === 'si' ? 'block' : 'none';
        });
    });
});