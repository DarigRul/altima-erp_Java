package com.altima.springboot.app.dto;

import com.altima.springboot.app.models.entity.ComercialTicket;

public class ComercialTicketDTO {
    
    private String title;
    private String start;
    private String end;
    private Long id;

    public ComercialTicketDTO(ComercialTicket ticket) {
        super();

        this.title = ticket.getDescripcion();
        this.start = ticket.getFechaInicio();
        this.end = ticket.getFechaFin();
        this.id = ticket.getIdTicket();

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}