#version 120

const int MAX_JOINTS = 50;//max joints allowed in a skeleton
const int MAX_WEIGHTS = 3;//max number of joints that can affect a vertex

in vec3 in_position;
in vec2 in_textureCoords;
in ivec3 in_jointIndices;
in vec3 in_weights;

varying vec2 pass_textureCoords;

uniform mat4 jointTransforms[MAX_JOINTS];

void main(void){

    vec4 totalLocalPos = vec4(0.0);

    for(int i=0;i<MAX_WEIGHTS;i++){
        mat4 jointTransform = jointTransforms[in_jointIndices[i]];
        vec4 posePosition = jointTransform * vec4(in_position, 1.0);
        totalLocalPos += posePosition * in_weights[i];
    }

    gl_Position = gl_ModelViewProjectionMatrix * totalLocalPos;
    pass_textureCoords = in_textureCoords;
}
