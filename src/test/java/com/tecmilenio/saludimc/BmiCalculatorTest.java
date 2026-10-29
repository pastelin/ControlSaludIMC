package com.tecmilenio.saludimc;

import com.tecmilenio.saludimc.service.BmiCalculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BmiCalculatorTest {
    @Test void calculaCorrectamente(){assertEquals(22.86,BmiCalculator.calcular(70,1.75));}
    @Test void rechazaCero(){assertThrows(IllegalArgumentException.class,()->BmiCalculator.calcular(0,1.75));}
    @Test void rechazaEstaturaFueraDeRango(){assertThrows(IllegalArgumentException.class,()->BmiCalculator.calcular(70,2.51));}
    @Test void clasificacion(){assertEquals("Saludable",BmiCalculator.clasificar(22.86));}
}
