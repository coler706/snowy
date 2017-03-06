package minithree.raw

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE")
object jsThree extends js.Object {
  var REVISION: String                               = js.native
  var CullFaceNone: CullFace                         = js.native
  var CullFaceBack: CullFace                         = js.native
  var CullFaceFront: CullFace                        = js.native
  var CullFaceFrontBack: CullFace                    = js.native
  var FrontFaceDirectionCW: FrontFaceDirection       = js.native
  var FrontFaceDirectionCCW: FrontFaceDirection      = js.native
  var BasicShadowMap: ShadowMapType                  = js.native
  var PCFShadowMap: ShadowMapType                    = js.native
  var PCFSoftShadowMap: ShadowMapType                = js.native
  var FrontSide: Side                                = js.native
  var BackSide: Side                                 = js.native
  var DoubleSide: Side                               = js.native
  var NoShading: Shading                             = js.native
  var FlatShading: Shading                           = js.native
  var SmoothShading: Shading                         = js.native
  var NoColors: Colors                               = js.native
  var FaceColors: Colors                             = js.native
  var VertexColors: Colors                           = js.native
  var NoBlending: Blending                           = js.native
  var NormalBlending: Blending                       = js.native
  var AdditiveBlending: Blending                     = js.native
  var SubtractiveBlending: Blending                  = js.native
  var MultiplyBlending: Blending                     = js.native
  var CustomBlending: Blending                       = js.native
  var AddEquation: BlendingEquation                  = js.native
  var SubtractEquation: BlendingEquation             = js.native
  var ReverseSubtractEquation: BlendingEquation      = js.native
  var ZeroFactor: BlendingDstFactor                  = js.native
  var OneFactor: BlendingDstFactor                   = js.native
  var SrcColorFactor: BlendingDstFactor              = js.native
  var OneMinusSrcColorFactor: BlendingDstFactor      = js.native
  var SrcAlphaFactor: BlendingDstFactor              = js.native
  var OneMinusSrcAlphaFactor: BlendingDstFactor      = js.native
  var DstAlphaFactor: BlendingDstFactor              = js.native
  var OneMinusDstAlphaFactor: BlendingDstFactor      = js.native
  var DstColorFactor: BlendingSrcFactor              = js.native
  var OneMinusDstColorFactor: BlendingSrcFactor      = js.native
  var SrcAlphaSaturateFactor: BlendingSrcFactor      = js.native
  var MultiplyOperation: Combine                     = js.native
  var MixOperation: Combine                          = js.native
  var AddOperation: Combine                          = js.native
  var UVMapping: MappingConstructor                  = js.native
  var CubeReflectionMapping: MappingConstructor      = js.native
  var CubeRefractionMapping: MappingConstructor      = js.native
  var SphericalReflectionMapping: MappingConstructor = js.native
  var SphericalRefractionMapping: MappingConstructor = js.native
  var RepeatWrapping: Wrapping                       = js.native
  var ClampToEdgeWrapping: Wrapping                  = js.native
  var MirroredRepeatWrapping: Wrapping               = js.native
  var NearestFilter: TextureFilter                   = js.native
  var NearestMipMapNearestFilter: TextureFilter      = js.native
  var NearestMipMapLinearFilter: TextureFilter       = js.native
  var LinearFilter: TextureFilter                    = js.native
  var LinearMipMapNearestFilter: TextureFilter       = js.native
  var LinearMipMapLinearFilter: TextureFilter        = js.native
  var UnsignedByteType: TextureDataType              = js.native
  var ByteType: TextureDataType                      = js.native
  var ShortType: TextureDataType                     = js.native
  var UnsignedShortType: TextureDataType             = js.native
  var IntType: TextureDataType                       = js.native
  var UnsignedIntType: TextureDataType               = js.native
  var FloatType: TextureDataType                     = js.native
  var UnsignedShort4444Type: PixelType               = js.native
  var UnsignedShort5551Type: PixelType               = js.native
  var UnsignedShort565Type: PixelType                = js.native
  var AlphaFormat: PixelFormat                       = js.native
  var RGBFormat: PixelFormat                         = js.native
  var RGBAFormat: PixelFormat                        = js.native
  var LuminanceFormat: PixelFormat                   = js.native
  var LuminanceAlphaFormat: PixelFormat              = js.native
  var RGB_S3TC_DXT1_Format: CompressedPixelFormat    = js.native
  var RGBA_S3TC_DXT1_Format: CompressedPixelFormat   = js.native
  var RGBA_S3TC_DXT3_Format: CompressedPixelFormat   = js.native
  var RGBA_S3TC_DXT5_Format: CompressedPixelFormat   = js.native
  var Math: Math                                     = js.native
  var LineStrip: LineType                            = js.native
  var LinePieces: LineType                           = js.native
  var ShaderChunk: ShaderChunk                       = js.native
}