/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.MSGFoundation.controller;

import com.MSGFoundation.model.Estudiante;
import com.MSGFoundation.model.Solicitud;
import com.MSGFoundation.repository.EstudianteRepository;
import com.MSGFoundation.repository.SolicitudRepository;
import com.MSGFoundation.service.CamundaService;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Luis Alfonso Medina
 */
@Controller
public class EstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private CamundaService camundaService; // Servicio que crearás para interactuar con Camunda



    @GetMapping("/estudiantes/nuevo")
    public String mostrarFormularioEstudiante(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "views/estudiante_form";
    }

    @PostMapping("/estudiantes/guardar")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante) {
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
        
        // Obtener el mes actual
        int mesActual = fechaActual.getMonthValue();
        estudiante.setMes_actual(mesActual);
        estudiante.setEstado("En proceso");
        // Guardar el estudiante en la base de datos
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante);
        
        // Llenar el formulario de solicitud y crear el proceso en Camunda
        camundaService.diligenciarFormularioSolicitud(estudianteGuardado);
        
        // Actualizar el estudiante con el ID del proceso (esto se gestiona dentro del método de Camunda)
        // No es necesario guardar el estudiante nuevamente aquí si el ID del proceso se gestiona dentro de Camunda.
        
        // Crear una solicitud asociada al estudiante
        Solicitud solicitud = new Solicitud();
        solicitud.setEstudiante(estudianteGuardado);
        solicitud.setTipo(1); // Define el tipo según tus necesidades
        solicitud.setCargada(false);
        solicitudRepository.save(solicitud);
        
        // Redirigir a la lista de estudiantes o a donde desees
        //return "redirect:/estudiantes/lista";
        return "redirect:/solicitudes";
    }

    @GetMapping("/estudiantes/lista")
    public String listarEstudiantes(Model model) {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        model.addAttribute("estudiantes", estudiantes);
        return "views/estudiante_list";
    }
}
