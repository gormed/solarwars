/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jme3.shader;

import com.jme3.renderer.Renderer;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.IntMap;
import com.jme3.util.IntMap.Entry;
import com.jme3.util.ListMap;
import com.jme3.util.NativeObject;
import java.util.ArrayList;
import java.util.Collection;

public final class Shader extends NativeObject {

    /**
     * 
     * @deprecated shader language now specified per shader source. See
     * {@link ShaderSource#setLanguage(String)
     */
    @Deprecated
    private String language;
    
    /**
     * A list of all shader sources currently attached.
     */
    private ArrayList<ShaderSource> shaderSourceList;

    /**
     * Maps uniform name to the uniform variable.
     */
    private ListMap<String, Uniform> uniforms;

    /**
     * Maps attribute name to the location of the attribute in the shader.
     */
    private IntMap<Attribute> attribs;

    /**
     * Type of shader. The shader will control the pipeline of it's type.
     */
    public static enum ShaderType {
        /**
         * Control fragment rasterization. (e.g color of pixel).
         */
        Fragment,

        /**
         * Control vertex processing. (e.g transform of model to clip space)
         */
        Vertex,

        /**
         * Control geometry assembly. (e.g compile a triangle list from input data)
         */
        Geometry;
    }

    /**
     * Shader source describes a shader object in OpenGL. Each shader source
     * is assigned a certain pipeline which it controls (described by it's type).
     */
    public static class ShaderSource extends NativeObject {

        ShaderType sourceType;
        String language;
        String name;
        String source;
        String defines;

        public ShaderSource(ShaderType type){
            super(ShaderSource.class);
            this.sourceType = type;
            if (type == null) {
                throw new NullPointerException("The shader type must be specified");
            }
        }
        
        protected ShaderSource(ShaderSource ss){
            super(ShaderSource.class, ss.id);
            // No data needs to be copied.
            // (This is a destructable clone)
        }

        public ShaderSource(){
            super(ShaderSource.class);
        }

        public void setName(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }

        public ShaderType getType() {
            return sourceType;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            if (language == null) {
                throw new NullPointerException("Shader language cannot be null");
            }
            this.language = language;
            setUpdateNeeded();
        }

        public void setSource(String source){
            if (source == null) {
                throw new NullPointerException("Shader source cannot be null");
            }
            this.source = source;
            setUpdateNeeded();
        }

        public void setDefines(String defines){
            if (defines == null) {
                throw new NullPointerException("Shader defines cannot be null");
            }
            this.defines = defines;
            setUpdateNeeded();
        }

        public String getSource(){
            return source;
        }

        public String getDefines(){
            return defines;
        }
        
        @Override
        public String toString(){
            String nameTxt = "";
            if (name != null)
                nameTxt = "name="+name+", ";
            if (defines != null)
                nameTxt += "defines, ";
            

            return getClass().getSimpleName() + "["+nameTxt+"type="
                                              + sourceType.name()+", language=" + language + "]";
        }

        public void resetObject(){
            id = -1;
            setUpdateNeeded();
        }

        public void deleteObject(Object rendererObject){
            ((Renderer)rendererObject).deleteShaderSource(ShaderSource.this);
        }

        public NativeObject createDestructableClone(){
            return new ShaderSource(ShaderSource.this);
        }
    }

    /**
     * @deprecated Shader sources are now associated with the shader
     * language.
     */
    @Deprecated
    public Shader(String language){
        super(Shader.class);
        this.language = language;
        initialize();
    }

    /**
     * Initializes the shader for use, must be called after the 
     * constructor without arguments is used.
     */
    public void initialize() {
        shaderSourceList = new ArrayList<ShaderSource>();
        uniforms = new ListMap<String, Uniform>();
        attribs = new IntMap<Attribute>();
    }
    
    /**
     * Creates a new shader, {@link #initialize() } must be called
     * after this constructor for the shader to be usable.
     */
    public Shader(){
        super(Shader.class);
    }

    /**
     * Do not use this constructor. Used for destructable clones only.
     */
    protected Shader(Shader s){
        super(Shader.class, s.id);
        
        // Shader sources cannot be shared, therefore they must
        // be destroyed together with the parent shader.
        shaderSourceList = new ArrayList<ShaderSource>();
        for (ShaderSource source : s.shaderSourceList){
            shaderSourceList.add( (ShaderSource)source.createDestructableClone() );
        }
    }

    /**
     * @deprecated Use the method that takes a language argument instead. 
     * {@link #addSource(com.jme3.shader.Shader.ShaderType, java.lang.String, java.lang.String, java.lang.String, java.lang.String) }
     */
    @Deprecated
    public void addSource(ShaderType type, String name, String source, String defines) {
        addSource(type, name, source, defines, this.language);
    }
    
    /**
     * @deprecated Use the method that takes a language argument instead. 
     * {@link #addSource(com.jme3.shader.Shader.ShaderType, java.lang.String, java.lang.String, java.lang.String, java.lang.String) }
     */
    @Deprecated
    public void addSource(ShaderType type, String source, String defines){
        addSource(type, null, source, defines);
    }

    /**
     * @deprecated Use the method that takes a language argument instead. 
     * {@link #addSource(com.jme3.shader.Shader.ShaderType, java.lang.String, java.lang.String, java.lang.String, java.lang.String) }
     */
    @Deprecated
    public void addSource(ShaderType type, String source){
        addSource(type, source, null);
    }

    /**
     * @deprecated Shader sources may not be shared.
     * {@link #addSource(com.jme3.shader.Shader.ShaderType, java.lang.String, java.lang.String, java.lang.String, java.lang.String) }
     */
    @Deprecated
    private void addSource(ShaderSource source){
        shaderSourceList.add(source);
        setUpdateNeeded();
    }
    
    /**
     * Adds source code to a certain pipeline.
     *
     * @param type The pipeline to control
     * @param source The shader source code (in GLSL).
     * @param defines Preprocessor defines (placed at the beginning of the shader)
     * @param language The shader source language, currently accepted is GLSL###
     * where ### is the version, e.g. GLSL100 = GLSL 1.0, GLSL330 = GLSL 3.3, etc.
     */
    public void addSource(ShaderType type, String name, String source, String defines, String language){
        ShaderSource shaderSource = new ShaderSource(type);
        shaderSource.setSource(source);
        shaderSource.setName(name);
        shaderSource.setLanguage(language);
        if (defines != null) {
            shaderSource.setDefines(defines);
        }
        shaderSourceList.add(shaderSource);
        setUpdateNeeded();
    }

    public Uniform getUniform(String name){
        Uniform uniform = uniforms.get(name);
        if (uniform == null){
            uniform = new Uniform();
            uniform.name = name;
            uniforms.put(name, uniform);
        }
        return uniform;
    }

    public void removeUniform(String name){
        uniforms.remove(name);
    }

    public Attribute getAttribute(VertexBuffer.Type attribType){
        int ordinal = attribType.ordinal();
        Attribute attrib = attribs.get(ordinal);
        if (attrib == null){
            attrib = new Attribute();
            attrib.name = attribType.name();
            attribs.put(ordinal, attrib);
        }
        return attrib;
    }

    public ListMap<String, Uniform> getUniformMap(){
        return uniforms;
    }

    public Collection<ShaderSource> getSources(){
        return shaderSourceList;
    }

    /**
     * @deprecated Shaders no longer have a language variable, 
     * use {@link ShaderSource#getLanguage() } instead.
     */
    @Deprecated
    public String getLanguage(){
        return language;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + 
                "[numSources=" + shaderSourceList.size() +
                ", numUniforms=" + uniforms.size() +
                ", shaderSources=" + getSources() + "]";
    }

    /**
     * @deprecated This method is not needed since deleting
     * a shader causes the sources to delete as well, thus its not required
     * for them to be GC'd to be removed from GL.
     */
    @Deprecated
    public void resetSources(){
        shaderSourceList.clear();
    }

    /**
     * @deprecated Unusable shaders cause the renderer to crash, 
     * therefore this field no longer serves any purpose.
     */
    @Deprecated
    public boolean isUsable(){
        return true;
    }

    /**
     * @deprecated Unusable shaders cause the renderer to crash, 
     * therefore this field no longer serves any purpose.
     */
    @Deprecated
    public void setUsable(boolean usable){
    }

    /**
     * Usually called when the shader itself changes or during any
     * time when the variable locations need to be refreshed.
     */
    public void resetLocations() {
        if (uniforms != null) {
            // NOTE: Shader sources will be reset seperately from the shader itself.
            for (Uniform uniform : uniforms.values()) {
                uniform.reset(); // fixes issue with re-initialization
            }
        }
        if (attribs != null) {
            for (Entry<Attribute> entry : attribs) {
                entry.getValue().location = ShaderVariable.LOC_UNKNOWN;
            }
        }
    }

    @Override
    public void setUpdateNeeded(){
        super.setUpdateNeeded();
        resetLocations();
    }

    /**
     * Called by the object manager to reset all object IDs. This causes
     * the shader to be reuploaded to the GPU incase the display was restarted.
     */
    @Override
    public void resetObject() {
        this.id = -1;
        for (ShaderSource source : shaderSourceList){
            source.resetObject();
        }
        setUpdateNeeded();
    }

    @Override
    public void deleteObject(Object rendererObject) {
        ((Renderer)rendererObject).deleteShader(this);
    }

    public NativeObject createDestructableClone(){
        return new Shader(this);
    }

}
