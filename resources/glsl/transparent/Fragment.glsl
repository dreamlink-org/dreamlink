#version 410

struct TextureMetaData {
    int texture_unit_id;
    int animation_total_frames;
    int animation_row_frames;
    int animation_start_frame;
    int animation_speed;
};

struct TerrainMetaData {
    int local_light;
    int portal_light;
    int is_hidden;
    int is_affected_by_light;
};

layout (location = 0) out vec4 transparency_accumulator;
layout (location = 1) out float transparency_reveal;

in vec2 vert_texture_offset;
flat in TextureMetaData vert_texture_metadata;
flat in TerrainMetaData vert_terrain_metadata;

uniform float base_light;
uniform float portal_light;
uniform int is_show_hidden;

uniform sampler2D sampler_entity;
uniform sampler2D sampler_zone;

const int entity_texture_unit = 0;
const int zone_texture_unit = 1;
const float epsilon = 0.00001;

vec4 sample_from_offset(vec2 offset, int unit) {
    switch(unit) {
        case entity_texture_unit:
            return texture(sampler_entity, offset);
        case zone_texture_unit:
            return texture(sampler_zone, offset);
        default:
            return vec4(1.0, 0.0, 0.0, 1.0);
    }
}

void main() {
    float total_light = base_light;
    total_light = max(total_light, float(vert_terrain_metadata.local_light) / 15.0);
    total_light = max(total_light, float(vert_terrain_metadata.portal_light) / 15.0 * portal_light);

    vec4 color = sample_from_offset(vert_texture_offset, vert_texture_metadata.texture_unit_id);
    if(vert_terrain_metadata.is_affected_by_light == 1) {
        color.rgb *= total_light;
    }

    float weight = clamp(pow(min(1.0, color.a * 10.0) + 0.01, 3.0) * 1e8 * 
                    pow(1.0 - gl_FragCoord.z * 0.9, 3.0), 1e-2, 3e3);

    if(vert_terrain_metadata.is_hidden == 1 && is_show_hidden == 0) {
        discard;
    }

    if(color.a < epsilon) {
        discard;
    }

    transparency_accumulator = vec4(color.rgb * color.a, color.a) * weight;
    transparency_reveal = color.a;
}
