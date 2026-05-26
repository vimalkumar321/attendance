package com.vimal.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vimal.app.entity.Worker;

public interface WorkerRepository extends JpaRepository<Worker, Long>{

}
