package com.demo.learning.saga.ShipmentService.command.api.repository;

import com.demo.learning.saga.ShipmentService.command.api.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}
