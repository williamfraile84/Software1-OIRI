package com.MSGFoundation.service;

import com.MSGFoundation.model.Estudiante;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpMethod;

@Service
public class CamundaServiceImpl implements CamundaService {

    @Value("${camunda.url:http://localhost:9004/engine-rest/}")
    private String camundaUrl;

    private final RestTemplate restTemplate;

    public CamundaServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void diligenciarFormularioSolicitud(Estudiante estudiante) {
        String processDefinitionKey = "MOIRI"; 

        // Crear un mapa de variables para iniciar el proceso
        Map<String, Object> variables = crearVariablesEstudiante(estudiante);

        // Crear el cuerpo de la solicitud para iniciar el proceso
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        // Iniciar el proceso en Camunda
        Map<String, Object> response = restTemplate.postForObject(
                camundaUrl + "process-definition/key/" + processDefinitionKey + "/start",
                requestBody,
                Map.class
        );

        // Obtener el ID del proceso iniciado
        String procesoId = response != null ? (String) response.get("id") : null;
        estudiante.setProceso(procesoId);

            
        completarTareaPorNombre(procesoId, "Diligenciar formulario de solicitud");
        if (procesoId != null) {
            // Completar la tarea "Diligenciar formulario de solicitud"
            completarTareaPorNombre(procesoId, "Diligenciar formulario de solicitud");
        } else {
            System.err.println("No se pudo iniciar el proceso en Camunda.");
        }
    }

    private Map<String, Object> crearVariablesEstudiante(Estudiante estudiante) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("codigoSolicitud", Map.of("value", estudiante.getId(), "type", "Long"));
        variables.put("nombreEstudiante", Map.of("value", estudiante.getNombre(), "type", "String"));
        variables.put("codigoEstudiantil", Map.of("value", estudiante.getCodigo(), "type", "String"));
        variables.put("estudianteId", Map.of("value", estudiante.getId(), "type", "Long"));
        variables.put("email", Map.of("value", estudiante.getEmail(), "type", "String"));
        return variables;
    }

    private void completarTareaPorNombre(String procesoId, String taskName) {
        try {
            String taskId = obtenerTaskId(procesoId, taskName);
            if (taskId != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(new HashMap<>(), headers);
                restTemplate.postForEntity(camundaUrl + "task/" + taskId + "/complete", requestEntity, String.class);
                System.out.println("Tarea '" + taskName + "' completada con éxito.");
            } else {
                System.err.println("No se encontró la tarea: " + taskName);
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Error al completar la tarea: " + e.getResponseBodyAsString());
        }
    }

    private String obtenerTaskId(String procesoId, String taskName) {
        String url = camundaUrl + "process-instance/" + procesoId + "/task";
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> tasks = response.getBody();
        if (tasks != null && !tasks.isEmpty()) {
            for (Map<String, Object> task : tasks) {
                if (taskName.equals(task.get("name"))) {
                    System.out.println("::::::::::::::::::::::::::::::::::::::Encontrada");
                    return (String) task.get("id");
                }
            }
        }
        return null;
    }

    @Override
    public void subirInformeMensual(String procesoId) {
        completarTareaPorNombre(procesoId, "Subir informe mensual");
    }

    public void subirInformeFinal(String procesoId) {
        completarTareaPorNombre(procesoId, "Subir informe final");
    }

    public void solicitarCertificadoNotas(String procesoId) {
        completarTareaPorNombre(procesoId, "Solicitar certificado de notas");
    }
}