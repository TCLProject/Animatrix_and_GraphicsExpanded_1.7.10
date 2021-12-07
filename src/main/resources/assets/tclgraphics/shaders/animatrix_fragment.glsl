#version 120

uniform sampler2D texture;

varying vec2 pass_textureCoords;
void main() {
    gl_FragColor = texture2D(texture, pass_textureCoords);
}