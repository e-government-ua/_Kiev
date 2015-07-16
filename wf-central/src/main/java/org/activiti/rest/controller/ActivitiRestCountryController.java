package org.activiti.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.dao.CountryDao;
import org.wf.dp.dniprorada.model.Country;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/services")
public class ActivitiRestCountryController {
    private static final Logger log = Logger.getLogger(ActivitiRestCountryController.class);

    @Autowired
    private CountryDao countryDao;

    //return true if all args are null
    private boolean areAllArgsNull(Object... args) {
        boolean result = true;
        for (Object o : args) {
            if (o != null){
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * отдает массив объектов сущности,
//     * подпадающих под критерии
//     * @param nID_UA (опциональный)
//     * @param sID_Two (опциональный)
//     * @param sID_Three (опциональный)
//     * @param sNameShort_UA (опциональный)
//     * @param sNameShort_EN (опциональный)
//     * @param response
     * @return list of countries according to filters
     */
    @RequestMapping(value = "/getCountries", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Country> getCountries(
//            @RequestParam(value = "nID_UA", required = false) Long nID_UA,
//            @RequestParam(value = "sID_Two", required = false) String sID_Two,
//            @RequestParam(value = "sID_Three", required = false) String sID_Three,
//            @RequestParam(value = "sNameShort_UA", required = false) String sNameShort_UA,
//            @RequestParam(value = "sNameShort_EN", required = false) String sNameShort_EN,
            HttpServletResponse response) {

        return countryDao.getAll();
    }

    /**
     * отдает элемент(по первому ненулевому из уникальных-ключей)
     * @param nID_UA (опциональный)
     * @param sID_Two (опциональный)
     * @param sID_Three (опциональный)
     * @param response
     * @return list of countries according to filters
     */
    @RequestMapping(value = "/getCountry", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<Country> getCountry(
            @RequestParam(value = "nID", required = false) Long nID,
            @RequestParam(value = "nID_UA", required = false) Long nID_UA,
            @RequestParam(value = "sID_Two", required = false) String sID_Two,
            @RequestParam(value = "sID_Three", required = false) String sID_Three,
            HttpServletResponse response) {

        if (areAllArgsNull(nID, nID_UA, sID_Two, sID_Three)){
            response.setStatus(403);
            response.setHeader("Reason", "required at least one of parameters " +
                    "(nID, nID_UA, sID_Two, sID_Three)!");
            return null;
        }
        ResponseEntity<Country> result = null;
        try {
            Country country = countryDao.getByKey(nID, nID_UA, sID_Two, sID_Three);
            result = JsonRestUtils.toJsonResponse(country);
        } catch (RuntimeException e) {
            response.setStatus(403);
            response.setHeader("Reason", e.getMessage());
        }
        return result;
    }

    /**
     * апдейтит элемент(если задан один из уникальных-ключей)
     * или вставляет (если не задан nID), и отдает экземпляр нового объекта
     * @param nID (опциональный, если другой уникальный-ключ задан и по нему найдена запись)
     * @param nID_UA (опциональный, если другой уникальный-ключ задан и по нему найдена запись)
     * @param sID_Two (опциональный, если другой уникальный-ключ задан и по нему найдена запись)
     * @param sID_Three (опциональный, если другой уникальный-ключ задан и по нему найдена запись)
     * @param sNameShort_UA (опциональный, если другой уникальный-ключ задан и по нему найдена запись)
     * @param sNameShort_EN (опциональный, если другой уникальный-ключ задан и по нему найдена запись)
     * @param sReference_LocalISO (опциональный)
     * @param response
     * @return
     */
    @RequestMapping(value = "/setCountry", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<Country> setCountry(
            @RequestParam(value = "nID", required = false) Long nID,
            @RequestParam(value = "nID_UA", required = false) Long nID_UA,
            @RequestParam(value = "sID_Two", required = false) String sID_Two,
            @RequestParam(value = "sID_Three", required = false) String sID_Three,
            @RequestParam(value = "sNameShort_UA", required = false) String sNameShort_UA,
            @RequestParam(value = "sNameShort_EN", required = false) String sNameShort_EN,
            @RequestParam(value = "sReference_LocalISO", required = false) String sReference_LocalISO,
            HttpServletResponse response) {

        if (areAllArgsNull(nID, nID_UA, sID_Two, sID_Three,
                sNameShort_UA, sNameShort_EN)){
            response.setStatus(403);
            response.setHeader("Reason", "required at least one of parameters " +
                    "(nID, nID_UA, sID_Two, sID_Three, sNameShort_UA, sNameShort_EN)!");
        }

        ResponseEntity<Country> result = null;

        try {
            Country country = countryDao.setCountry(nID, nID_UA, sID_Two, sID_Three,
                    sNameShort_UA, sNameShort_EN, sReference_LocalISO);
            result = JsonRestUtils.toJsonResponse(country);
        } catch (RuntimeException e){
            response.setStatus(403);
            response.setHeader("Reason", e.getMessage());
        }
        return result;
    }

    /**
     * удаляет элемент(по одому из уникальных ключей)
     * @param nID -- опциональный, если другой уникальный-ключ задан и по нему найдена запись
     * @param nID_UA-- опциональный
     * @param sID_Two-- опциональный
     * @param sID_Three-- опциональный
     * @param response
     */
    @RequestMapping(value = "/removeCountry", method = RequestMethod.GET)
    public
    @ResponseBody
    void removeCountry(
            @RequestParam(value = "nID", required = false) Long nID,
            @RequestParam(value = "nID_UA", required = false) Long nID_UA,
            @RequestParam(value = "sID_Two", required = false) String sID_Two,
            @RequestParam(value = "sID_Three", required = false) String sID_Three,
           HttpServletResponse response) {

        ResponseEntity<Country> result = null;

        if (areAllArgsNull(nID_UA, sID_Two, sID_Three)){
            response.setStatus(403);
            response.setHeader("Reason", "required at least one of parameters " +
                    "(nID, nID_UA, sID_Two, sID_Three)!");
        }

        try {
            countryDao.removeByKey(nID, nID_UA, sID_Two, sID_Three);
        } catch (RuntimeException e){
            response.setStatus(403);
            response.setHeader("Reason", e.getMessage());
        }
    }
 }
