package org.yellowbinary.cms.server.core.controller;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.yellowbinary.cms.server.core.*;
import org.yellowbinary.cms.server.core.context.Context;
import org.yellowbinary.cms.server.core.dao.ConfigurationDao;
import org.yellowbinary.cms.server.core.service.AliasService;
import org.yellowbinary.cms.server.core.service.NodeService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "/node", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public class NodeController {

    private static final Logger LOG = LoggerFactory.getLogger(NodeController.class);

    @Autowired
    private ConfigurationDao configurationDao;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private AliasService aliasService;

    @RequestMapping("/")
    public Node getNode() throws NodeNotFoundException, NodeLoadException, ModuleException {

        String startPageId = configurationDao.readValue(String.class, Core.Settings.START_PAGE);

        return nodeService.load(startPageId);
    }

    @RequestMapping("/{key}")
    public Node getNode(@PathVariable String key) throws NodeNotFoundException, NodeLoadException, ModuleException {
        return nodeService.load(key, 0);
    }

    @RequestMapping("/{key}/{version}")
    public Node getNode(@PathVariable String key, @PathVariable Integer version) throws NodeNotFoundException, NodeLoadException, ModuleException {
        return nodeService.load(key, version);
    }


    @ExceptionHandler(NodeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(NodeNotFoundException exception) {
        LOG.info("Node not found: " + exception.nodeId);

        String pageNotFoundKey = configurationDao.readValue(String.class, Core.Settings.PAGE_NOT_FOUND_PAGE);
        String url = aliasService.getAliasForKey(pageNotFoundKey, "page-not-found");
        if (Context.current().getAttribute("not_found") == null) {
            Context.current().addAttribute("not_found", Boolean.TRUE);
            LOG.debug("Sending reference to Page-Not-Found Page");
            return createResultMap(exception.nodeId, url, null);
        }
        LOG.warn("Using fallback not-found handling, sending 404 with no content");
        return Collections.emptyMap();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleError(Exception exception) {
        String message = "Error loading node: " + exception.getMessage();
        LOG.info(message, exception);

        String internalServerErrorPage = configurationDao.readValue(String.class, Core.Settings.INTERNAL_SERVER_ERROR_PAGE);
        String url = aliasService.getAliasForKey(internalServerErrorPage, "error");

        if (Context.current().getAttribute("error") == null) {
            Context.current().addAttribute("error", Boolean.TRUE);
            LOG.debug("Sending reference to Internal-Server-Error Page");
            return createResultMap(null, url, message);
        }
        LOG.warn("Using fallback error handling, sending 501 with no content");
        return Collections.emptyMap();
    }

    private Map<String, String> createResultMap(String key, String reference, String message) {
        Map<String, String> result = Maps.newHashMap();
        if (StringUtils.isNotBlank(key)) {
            result.put("key", key);
        }
        result.put("reference", reference);
        if (StringUtils.isNotBlank(message)) {
            result.put("message", message);
        }
        return result;
    }

}
