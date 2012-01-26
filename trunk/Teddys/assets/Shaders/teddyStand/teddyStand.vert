varying vec2 texCoord;
uniform int m_SelectedTile;
uniform int m_MaxTiles;
uniform bool m_Mirrored;

uniform mat4 g_WorldViewProjectionMatrix;
attribute vec3 inPosition;
attribute vec2 inTexCoord;

void main() { 
    float tileSelector;
    float curTile = float(m_SelectedTile);

    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
    texCoord = inTexCoord;


    if(m_Mirrored) {
        curTile = float(m_MaxTiles) - 1.0 - curTile;
    }
    

    if(curTile < 1.0) {
        tileSelector = curTile;
    } else {
        tileSelector = curTile / float(m_MaxTiles);
    }
    
    texCoord.s = texCoord.s / float(m_MaxTiles) + tileSelector;
    
    if(m_Mirrored) {
        texCoord.s = 1.0 - texCoord.s;
    }
}