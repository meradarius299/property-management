package com.mycompany.property_management.controller;

import com.mycompany.property_management.dto.CalculatorDTO;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/calculator")//class level mapping of URL to a controller class
public class CalculatorController {
    //http://localhost:8080/api/v1/calculator/add
    //http://localhost:8080/api/v1/calculator/add?a=14.2&b=13.3
    @GetMapping("/add")//method level mappinf of a URL to a controller func
    public Double add(@RequestParam("a") Double a, @RequestParam("b") Double b){
        return a + b;
    }

    @GetMapping("/sub/{a}/{b}")//Map the values of URL to java vars by Path vars method
    public Double sub(@PathVariable("a") Double a, @PathVariable("b") Double b){
        //http://localhost:8080/api/v1/calculator/sub/4.3/3.4
        Double res = null;
        if(a < b){
            res = a - b;
        }
        else {
            res = b - a;
        }
        return res;
    }

    @PostMapping("/mul")
    public ResponseEntity mul(@RequestBody CalculatorDTO calculatorDTO){
        //http://localhost:8080/api/v1/calculator/mul
        Double res = null;
        res = calculatorDTO.getA() * calculatorDTO.getB() * calculatorDTO.getC() * calculatorDTO.getD();
        ResponseEntity<Double> responseEntity = new ResponseEntity<Double>(res, HttpStatus.CREATED);
        return responseEntity;
    }
}
