/*
 * The MIT License
 *
 * Copyright 2013 Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.synopsys.arc.jenkins.plugins.ownership;

import hudson.Plugin;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Contains global actions and configurations
 * @since 0.0.1
 * @author Oleg Nenashev <nenashev@synopsys.com>
 */
public class OwnershipPlugin extends Plugin {
    private static String DEFAULT_EMAIL_SUFFIX="@unknown.unknown";
    
    /**
     * @deprecated Isn't used in current version
     */
    private boolean enableSlaveOwnership = false;
    /**
     * @deprecated Isn't used in current version
     */
    private boolean enableJobOwnership = true;
    
    /**
     * E-mail suffix for mailto:// fields
     * @since 0.0.4
     */
    private String emailSuffix = DEFAULT_EMAIL_SUFFIX;
    
    public String getEmailSuffix()
    {
        return emailSuffix;
    }
    
    private List<OwnershipAction> pluginActions = new ArrayList<OwnershipAction>();
    
    public static OwnershipPlugin Instance()
    {
        Plugin plugin = Jenkins.getInstance().getPlugin(OwnershipPlugin.class);
        return plugin != null ? (OwnershipPlugin)plugin : null;
    }
    
    @Override 
    public void start() throws Exception {
	super.load();
        ReinitActionsList();
	Hudson.getInstance().getActions().addAll(pluginActions);
    }
    
    @Override 
    public void configure(StaplerRequest req, JSONObject formData)
	    throws IOException, ServletException, Descriptor.FormException {
	Hudson.getInstance().getActions().removeAll(pluginActions);
        
        enableSlaveOwnership = false;
        enableJobOwnership = true;             
        ReinitActionsList();
        emailSuffix = formData.getString("emailSuffix");
        	
	save();
        Hudson.getInstance().getActions().addAll(pluginActions);
    }
   
    
    public void ReinitActionsList()
    {
        pluginActions.clear();
    }
}