package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.controllers.RestResponseEntityExceptionHandler;
import guru.springfamework.services.ResourceNotFoundException;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static guru.springfamework.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorControllerTest {

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController vendorController;

    MockMvc mockMvc;

    @Before
    public  void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(vendorController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void testListVendors() throws Exception {
        //given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName("ryan");
        vendor1.setVendorUrl("/api/v1/vendors/1");

        VendorDTO vendor2 = new VendorDTO();
        vendor2.setName("alex");
        vendor2.setVendorUrl("/api/v1/vendors/2");

        when(vendorService.getAllVendors()).thenReturn(new VendorListDTO(Arrays.asList(vendor1, vendor2)));

        mockMvc.perform(get("/api/v1/vendors")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void testGetVendorsById() throws Exception {
        //given
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName("ryan");
        vendor1.setVendorUrl("/api/v1/vendors/1");

        when(vendorService.getVendorById(anyLong())).thenReturn(vendor1);

        mockMvc.perform(get("/api/v1/vendors/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("ryan")));
    }

    @Test
    public void createNewVendor() throws Exception {
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName("Olivia");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendor1.getName());
        returnDTO.setVendorUrl("/api/v1/vendors/1");


        when(vendorService.createNewVendor(vendor1)).thenReturn(returnDTO);

        mockMvc.perform(post("/api/v1/vendors")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendor1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("Olivia")))
                .andExpect(jsonPath("$.vendor_url", equalTo("/api/v1/vendors/1")));
    }

    @Test
    public void testUpdateVendor() throws Exception {
        VendorDTO vendor = new VendorDTO();
        vendor.setName("Fred");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendor.getName());
        returnDTO.setVendorUrl("/api/v1/vendors/1");

        when(vendorService.saveVendorByDTO(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(put("/api/v1/vendors/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendor)))
                .andExpect(jsonPath("$.name", equalTo("Fred")))
                .andExpect(jsonPath("$.vendor_url", equalTo("/api/v1/vendors/1")));
    }

    @Test
    public void testPatchVendor() throws Exception {
        //given
        VendorDTO vendor = new VendorDTO();
        vendor.setName("tommy");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendor.getName());
        returnDTO.setVendorUrl("/api/v1/vendors/1");

        when(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch("/api/v1/vendors/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendor)))
                .andExpect(jsonPath("$.name", equalTo("tommy")))
                .andExpect(jsonPath("$.vendor_url", equalTo("/api/v1/vendors/1")));
    }

    @Test
    public void testDeleteVendor() throws Exception {
        mockMvc.perform(delete("/api/v1/vendors/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(vendorService).deleteVendorById(anyLong());
    }

    @Test
    public void testNotFoundException() throws Exception {
        when(vendorService.getVendorById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/v1/vendors/222")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
