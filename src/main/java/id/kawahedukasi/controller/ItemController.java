package id.kawahedukasi.controller;
import com.opencsv.exceptions.CsvValidationException;
import net.sf.jasperreports.engine.JRException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import id.kawahedukasi.dto.FileFromDTO;
import id.kawahedukasi.service.ExportService;
import id.kawahedukasi.service.ImportService;
import id.kawahedukasi.service.ItemService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;


@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemController {
    @Inject
    ItemService itemService;
    @Inject
    ExportService exportService;
    @Inject
    ImportService importService;
    @GET
    public Response get(){
        return itemService.get();
    }//List All Item

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id){
        return itemService.get(id);
    }// Detail Item by id

    @GET
    @Path("/export/pdf")
    @Produces("application/pdf")
    public Response exportPdf() throws JRException{
        return exportService.exportPdfItem();
    }
    @GET
    @Path("/export/excel")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response exportExcel() throws IOException {
        return exportService.exportExcelItem();
    }
    @GET
    @Path("/export/csv")
    @Produces("text/csv")
    public Response exportCsv() throws IOException {
        return exportService.exportCsvItem();
    }
    @POST
    public Response post(Map<String, Object> request){
        return itemService.post(request);
    }

    @POST
    @Path("/import/excel")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importExcel(@MultipartForm FileFromDTO request) {
        try{
            return importService.importExcel(request);
        } catch (IOException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @POST
    @Path("/import/csv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importCSV(@MultipartForm FileFromDTO request) {
        try{
            return importService.importCsv(request);
        } catch (IOException | CsvValidationException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PUT
    @Path("/{id}")
    @Transactional
    public Response put(@PathParam("id") Long id, Map<String, Object> request){
        return itemService.put(id, request);
    }
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return itemService.delete(id);
    }
}
