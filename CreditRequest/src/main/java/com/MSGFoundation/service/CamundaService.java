/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.MSGFoundation.service;

/**
 *
 * @author Luis Alfonso Medina
 */

import com.MSGFoundation.model.Estudiante;

public interface CamundaService {
    void diligenciarFormularioSolicitud(Estudiante estudiante);
    void subirInformeMensual(String procesoId);
}