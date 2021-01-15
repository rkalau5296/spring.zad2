package pl.spring.zad3.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spring.zad3.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/vehicles")
public class VehicleApi {
    private final List<Vehicle> vehicles;

    public VehicleApi() {
        this.vehicles = new ArrayList<>();
        vehicles.add(new Vehicle(1L, "BMW","850","black"));
        vehicles.add(new Vehicle(2L, "Ford","Fiesta","white"));
        vehicles.add(new Vehicle(3L, "Fiat","Panda","blue"));
    }


    @GetMapping(produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })

    public ResponseEntity<Resources<Vehicle>> getVehicles(){
        vehicles.forEach(vehicle -> vehicle.add(linkTo(VehicleApi.class).slash(vehicle.getVehicleId()).withSelfRel()));
        Resources<Vehicle> vehicleResources = new Resources<>(vehicles, linkTo(Vehicle.class).withSelfRel());
        return new ResponseEntity<>(vehicleResources, HttpStatus.OK);
    }

    @GetMapping(value ="/{id}", produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })

    public ResponseEntity<Resource<Vehicle>> getVehicleById(@PathVariable Long id){
        Link link = linkTo(VehicleApi.class).slash(id).withSelfRel();
        Optional<Vehicle> first = vehicles.stream().filter(vehicle -> vehicle.getVehicleId() == id).findFirst();
        Resource<Vehicle> vehicleResource = new Resource<>(first.get(), link);
        if(first.isPresent()) {
            return new ResponseEntity<>(vehicleResource, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/vehicle/{color}", produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Resources<Vehicle>> getVehicleByColor(@PathVariable String color){
        List<Vehicle> vehicleByColor = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            if (vehicle.getColor().equals(color))
                vehicleByColor.add(vehicle);
        }
        vehicleByColor.forEach(vehicle -> vehicle.add(linkTo(VehicleApi.class).slash(vehicle.getVehicleId()).withSelfRel()));
        vehicleByColor.forEach(vehicle -> vehicle.add(linkTo(VehicleApi.class).withRel("allColors")));
        Resources<Vehicle> vehicleResources = new Resources<>(vehicleByColor, linkTo(Vehicle.class).withSelfRel());
        return new ResponseEntity<>(vehicleResources, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addVehicle(@RequestBody Vehicle vehicle) {
        boolean add = vehicles.add(vehicle);
        if(add){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PutMapping
    public ResponseEntity modVehicle(@RequestBody Vehicle newVehicle) {
        Optional<Vehicle> first = vehicles.stream().filter(vehicle -> vehicle.getId() == newVehicle.getId()).findFirst();
        if(first.isPresent()) {
            vehicles.remove(first.get());
            vehicles.add(newVehicle);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/{id}/{color}")
    public ResponseEntity modOneParamVehicle(@PathVariable Long id, @PathVariable String color) {
        Optional<Vehicle> first = vehicles.stream().filter(vehicle -> vehicle.getVehicleId() == id).findFirst();
        if(first.isPresent()) {

            vehicles.removeIf(vehicle->vehicle.getVehicleId()==id);
            vehicles.add(new Vehicle(first.get().getVehicleId(), first.get().getBrand(), first.get().getModel(), color));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value= "/{id}", produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity removeVehicle(@PathVariable Long id) {
        Optional<Vehicle> first = vehicles.stream().filter(vehicle -> vehicle.getVehicleId() == id).findFirst();
        if(first.isPresent()) {
            vehicles.remove(first.get());
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
