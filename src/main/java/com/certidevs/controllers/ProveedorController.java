package com.certidevs.controllers;

import com.certidevs.entities.Proveedor;
import com.certidevs.repositories.ProductoRepository;
import com.certidevs.repositories.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor // genera constructor automáticamente para los campos final
@Controller
public class ProveedorController {

    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;

    // listar todos los proveedores
    @GetMapping("/proveedores")
    public String findAll(Model model) {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        model.addAttribute("proveedores", proveedores);

        return "proveedor/proveedor-list";
    }

    // detalle de proveedor
    @GetMapping("/proveedores/{id}")
    public String findById(Model model, @PathVariable Long id) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);

        if (proveedorOpt.isPresent()) {
            model.addAttribute("proveedor", proveedorOpt.get());
        } else {
            model.addAttribute("error", "No se ha encontrado proveedor");
        }

        return "proveedor/proveedor-detail"; // TODO: crear proveedor-detail.html
    }

    // mostrar formulario para crear nuevo proveedor
    @GetMapping("/proveedores/nuevo")
    public String createForm(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("productos", productoRepository.findAll());

        return "proveedor/proveedor-form";
    }

    // mostrar formulario para editar proveedor existente
    @GetMapping("/proveedores/{id}/editar")
    public String editForm(Model model, @PathVariable Long id) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);

        if (proveedorOpt.isPresent()) {
            model.addAttribute("proveedor", proveedorOpt.get());
            model.addAttribute("productos", productoRepository.findAll());
        } else {
            model.addAttribute("error", "No se ha encontrado proveedor");
        }

        return "proveedor/proveedor-form";
    }

    // procesar formulario (crear o actualizar)
    @PostMapping("/proveedores")
    public String saveForm(@ModelAttribute Proveedor proveedor) {
        proveedorRepository.save(proveedor);

        return "redirect:/proveedores";
    }

    // eliminar proveedor
    @PostMapping("/proveedores/{id}/eliminar")
    public String delete(@PathVariable Long id) {
        proveedorRepository.deleteById(id);

        return "redirect:/proveedores";
    }

    // FILTROS
    // filtrar por proveedores activos
    @GetMapping("/proveedores/activos")
    public String findActivos(Model model) {
        List<Proveedor> proveedores = proveedorRepository.findByActivoTrue();
        model.addAttribute("proveedores", proveedores);

        return "proveedor/proveedor-list";
    }

    // filtrar por proveedores inactivos
    @GetMapping("/proveedores/inactivos")
    public String findInactivos(Model model) {
        List<Proveedor> proveedores = proveedorRepository.findByActivoFalse();
        model.addAttribute("proveedores", proveedores);

        return "proveedor/proveedor-list";
    }
}