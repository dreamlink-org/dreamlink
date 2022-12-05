#version 450

struct TextureMetaData {
    int texture_unit_id;
    int animation_total_frames;
    int animation_row_frames;
    int animation_start_frame;
    int animation_speed;
};

layout (location = 0) out vec4 opaque;

in vec2 vert_texture_offset;
flat in TextureMetaData vert_texture_metadata;
flat in vec4 vert_color;

uniform sampler2D sampler_overlay;
uniform sampler2D sampler_entity;
uniform sampler2D sampler_zone;

const int entity_texture_unit = 0;
const int zone_texture_unit = 1;
const int overlay_texture_unit = 2;

vec4 sample_from_offset(vec2 offset, int unit) {
    switch(unit) {
        case entity_texture_unit:
            return texture(sampler_entity, offset);
        case zone_texture_unit:
            return texture(sampler_zone, offset);
        case overlay_texture_unit:
            return texture(sampler_overlay, offset);
        default:
            return vec4(1.0, 0.0, 0.0, 1.0);
    }
};

void main() {
    opaque = sample_from_offset(vert_texture_offset, vert_texture_metadata.texture_unit_id);
    opaque *= vert_color;
}
