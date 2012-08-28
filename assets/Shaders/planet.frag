
uniform sampler2D m_ColorMap;
uniform sampler2D m_TransformMap;
uniform sampler2D m_HaloMap;
uniform vec4 m_Color;
uniform float m_Shift;
uniform float m_VerticalShift;

varying vec2 texCoord;


void main(){
		vec4 tform = texture2D(m_TransformMap,texCoord);
		
		tform.y += m_Shift;
		tform.x += m_VerticalShift;

		vec4 color = texture2D(m_ColorMap, tform);
		color.w = tform.z*(color.x+0.5);
		
		color += texture2D(m_HaloMap,texCoord).x;
		
        gl_FragColor = color*m_Color;

}
