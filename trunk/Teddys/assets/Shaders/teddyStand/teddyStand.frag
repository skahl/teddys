varying vec2 texCoord;
uniform sampler2D m_TexMap;

void main() {
    

    gl_FragColor = texture2D(m_TexMap, texCoord.st);

}

