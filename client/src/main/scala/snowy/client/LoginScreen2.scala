package snowy.client

import minithree.THREE
import minithree.THREE.MeshPhongMaterialParameters
import org.scalajs.dom._
import snowy.client.ThreeMain._
import snowy.connection.GameState
import snowy.playfield._

import scala.scalajs.js
import scala.scalajs.js.Dynamic
import scala.scalajs.js.annotation.JSName

@js.native
@JSName("THREE.ConeGeometry")
class ConeGeometry(radius: Double = js.native,
                   height: Double = js.native,
                   radialSegments: Int = js.native,
                   heightSegments: Int = js.native,
                   openEnded: Boolean = js.native,
                   thetaStart: Double = js.native,
                   thetaLength: Double = js.native)
    extends THREE.Geometry {
  var parameters: js.Any = js.native
}

object LoginScreen2 {
  object Geos {
    val trunkGeo     = new THREE.BoxGeometry(2, 10, 2)
    val leave1Geo    = new ConeGeometry(4, 8, 4, 1, false, 0.783, Math.PI * 2)
    val leave2Geo    = new ConeGeometry(3, 4, 4, 1, false, 0.8, Math.PI * 2)
    val cardBigGeo   = new THREE.BoxGeometry(5, 8, 1)
    val cardSmallGeo = new THREE.BoxGeometry(4, 6, 1)

    val geoParams = Dynamic.literal(
      steps = 1,
      amount = 3,
      bevelEnabled = false
    )

    val geoS = new THREE.ExtrudeGeometry(Shapes.shapeS, geoParams)
    val geoN = new THREE.ExtrudeGeometry(Shapes.shapeN, geoParams)
    val geoO = new THREE.ExtrudeGeometry(Shapes.shapeO, geoParams)
    val geoW = new THREE.ExtrudeGeometry(Shapes.shapeW, geoParams)
    val geoY = new THREE.ExtrudeGeometry(Shapes.shapeY, geoParams)

    val inputGeo = new THREE.BoxGeometry(45, 10, 2)
  }
  object Mats {
    val trunkMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x502A2A, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )

    val leave1Mat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x658033, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val leave2Mat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0x81A442, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
    val cardMat = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0xd8bc9d, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )

    val matLetters = new THREE.MeshPhongMaterial(
      Dynamic
        .literal(color = 0xa3a3a3, shading = THREE.FlatShading)
        .asInstanceOf[MeshPhongMaterialParameters]
    )
  }
  object Shapes {
    val s = 3

    val shapeS = new THREE.Shape()
    shapeS.moveTo(s * 0 - s * 1.5, s * 0 - s * 2.5)
    shapeS.lineTo(s * 3 - s * 1.5, s * 0 - s * 2.5)
    shapeS.lineTo(s * 3 - s * 1.5, s * 1 - s * 2.5)
    shapeS.lineTo(s * 1 - s * 1.5, s * 1 - s * 2.5)
    shapeS.lineTo(s * 1 - s * 1.5, s * 2 - s * 2.5)
    shapeS.lineTo(s * 3 - s * 1.5, s * 2 - s * 2.5)
    shapeS.lineTo(s * 3 - s * 1.5, s * 5 - s * 2.5)
    shapeS.lineTo(s * 0 - s * 1.5, s * 5 - s * 2.5)
    shapeS.lineTo(s * 0 - s * 1.5, s * 4 - s * 2.5)
    shapeS.lineTo(s * 2 - s * 1.5, s * 4 - s * 2.5)
    shapeS.lineTo(s * 2 - s * 1.5, s * 3 - s * 2.5)
    shapeS.lineTo(s * 0 - s * 1.5, s * 3 - s * 2.5)
    shapeS.lineTo(s * 0 - s * 1.5, s * 0 - s * 2.5)

    val shapeN = new THREE.Shape()
    shapeN.moveTo(s * 0 - s * 2, s * 0 - s * 2.5)
    shapeN.lineTo(s * 2 - s * 2, s * 0 - s * 2.5)
    shapeN.lineTo(s * 3 - s * 2, s * 4 - s * 2.5)
    shapeN.lineTo(s * 3 - s * 2, s * 0 - s * 2.5)
    shapeN.lineTo(s * 4 - s * 2, s * 0 - s * 2.5)
    shapeN.lineTo(s * 4 - s * 2, s * 5 - s * 2.5)
    shapeN.lineTo(s * 2 - s * 2, s * 5 - s * 2.5)
    shapeN.lineTo(s * 1 - s * 2, s * 1 - s * 2.5)
    shapeN.lineTo(s * 1 - s * 2, s * 5 - s * 2.5)
    shapeN.lineTo(s * 0 - s * 2, s * 5 - s * 2.5)
    shapeN.lineTo(s * 0 - s * 2, s * 0 - s * 2.5)

    val shapeO = new THREE.Shape()
    shapeO.moveTo(s * 0 - s * 2, s * 0 - s * 2.5)
    shapeO.lineTo(s * 4 - s * 2, s * 0 - s * 2.5)
    shapeO.lineTo(s * 4 - s * 2, s * 5 - s * 2.5)
    shapeO.lineTo(s * 0 - s * 2, s * 5 - s * 2.5)
    shapeO.lineTo(s * 0 - s * 2, s * 0 - s * 2.5)
    shapeO.lineTo(s * 1 - s * 2, s * 1 - s * 2.5)
    shapeO.lineTo(s * 1 - s * 2, s * 4 - s * 2.5)
    shapeO.lineTo(s * 3 - s * 2, s * 4 - s * 2.5)
    shapeO.lineTo(s * 3 - s * 2, s * 1 - s * 2.5)
    shapeO.lineTo(s * 1 - s * 2, s * 1 - s * 2.5)
    shapeO.lineTo(s * 0 - s * 2, s * 0 - s * 2.5)

    val shapeW = new THREE.Shape()
    shapeW.moveTo(s * 0 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 1 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 1 - s * 2.5, s * 4 - s * 2.5)
    shapeW.lineTo(s * 2 - s * 2.5, s * 4 - s * 2.5)
    shapeW.lineTo(s * 2 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 3 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 3 - s * 2.5, s * 4 - s * 2.5)
    shapeW.lineTo(s * 4 - s * 2.5, s * 4 - s * 2.5)
    shapeW.lineTo(s * 4 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 5 - s * 2.5, s * 0 - s * 2.5)
    shapeW.lineTo(s * 5 - s * 2.5, s * 5 - s * 2.5)
    shapeW.lineTo(s * 0 - s * 2.5, s * 5 - s * 2.5)
    shapeW.lineTo(s * 0 - s * 2.5, s * 0 - s * 2.5)

    val shapeY = new THREE.Shape()
    shapeY.moveTo(s * 0 - s * 2, s * 0 - s * 2.5)
    shapeY.lineTo(s * 1 - s * 2, s * 0 - s * 2.5)
    shapeY.lineTo(s * 2 - s * 2, s * 2 - s * 2.5)
    shapeY.lineTo(s * 3 - s * 2, s * 0 - s * 2.5)
    shapeY.lineTo(s * 4 - s * 2, s * 0 - s * 2.5)
    shapeY.lineTo(s * 2.5 - s * 2, s * 3 - s * 2.5)
    shapeY.lineTo(s * 2.5 - s * 2, s * 5 - s * 2.5)
    shapeY.lineTo(s * 1.5 - s * 2, s * 5 - s * 2.5)
    shapeY.lineTo(s * 1.5 - s * 2, s * 3 - s * 2.5)
    shapeY.lineTo(s * 0 - s * 2, s * 0 - s * 2.5)
  }

  object Meshes {
    val trunk  = new THREE.Mesh(Geos.trunkGeo, Mats.trunkMat)
    val leave1 = new THREE.Mesh(Geos.leave1Geo, Mats.leave1Mat)
    val leave2 = new THREE.Mesh(Geos.leave2Geo, Mats.leave2Mat)

    val arrow1 = leave1.clone()
    val arrow2 = leave2.clone()

    val card1 = new THREE.Mesh(Geos.cardSmallGeo, Mats.cardMat)
    val card2 = new THREE.Mesh(Geos.cardBigGeo, Mats.cardMat)
    val card3 = new THREE.Mesh(Geos.cardSmallGeo, Mats.cardMat)

    val meshS = new THREE.Mesh(Geos.geoS, Mats.matLetters)
    val meshN = new THREE.Mesh(Geos.geoN, Mats.matLetters)
    val meshO = new THREE.Mesh(Geos.geoO, Mats.matLetters)
    val meshW = new THREE.Mesh(Geos.geoW, Mats.matLetters)
    val meshY = new THREE.Mesh(Geos.geoY, Mats.matLetters)

    val input = new THREE.Mesh(Geos.inputGeo, Mats.matLetters)
  }

  object Groups {
    val arrow     = new THREE.Object3D()
    val selector  = new THREE.Object3D()
    val tree      = new THREE.Object3D()
    val tree2     = new THREE.Object3D()
    val snowyText = new THREE.Object3D()
  }

  val amb   = new THREE.AmbientLight(0x888888)
  val light = new THREE.DirectionalLight(0xffffff)

  def positions(): Unit = {
    light.position.set(0, 20, 10)

    Meshes.trunk.position.y = 5
    Meshes.leave1.position.y = 14
    Meshes.leave2.position.y = 16

    Groups.tree.rotation.z = 0.5 * Math.PI
    Groups.tree2.rotation.z = 1.5 * Math.PI

    Meshes.card1.position.set(-6, 4, 0)
    Meshes.card2.position.set(0, 5, 0)
    Meshes.card3.position.set(6, 4, 0)

    Groups.selector.position.y = -70
    Groups.selector.rotation.x = 0.3 * Math.PI
    Groups.selector.scale.x = 2
    Groups.selector.scale.y = 2
    Groups.selector.scale.z = 2

    Meshes.meshS.position.x = -Shapes.s * 0
    Meshes.meshN.position.x = -Shapes.s * 4
    Meshes.meshO.position.x = -Shapes.s * 9
    Meshes.meshW.position.x = -Shapes.s * 14
    Meshes.meshY.position.x = -Shapes.s * 19

    Meshes.meshW.rotation.x = 1 * Math.PI
    Meshes.meshW.position.z = 3

    Meshes.meshY.rotation.x = 1 * Math.PI
    Meshes.meshY.position.z = 3

    Groups.snowyText.position.x = Shapes.s * 10
    Groups.snowyText.position.y = 48

    Groups.snowyText.rotation.x = 0.3 * Math.PI

    Meshes.input.rotation.x = 0.4 * Math.PI

    Meshes.arrow2.position.y = 2

    Groups.arrow.position.x = -52
    Groups.arrow.rotation.z = 0.5 * Math.PI
    Groups.arrow.scale.set(3, 4, 3)
  }

  def addGroups(): Unit = {
    Groups.tree.add(Meshes.trunk)
    Groups.tree.add(Meshes.leave1)
    Groups.tree.add(Meshes.leave2)

    Groups.tree2.add(Meshes.trunk.clone())
    Groups.tree2.add(Meshes.leave1.clone())
    Groups.tree2.add(Meshes.leave2.clone())

    Groups.arrow.add(Meshes.arrow1)
    Groups.arrow.add(Meshes.arrow2)

    Groups.selector.add(Groups.tree)
    Groups.selector.add(Groups.tree2)

    Groups.selector.add(Meshes.card1)
    Groups.selector.add(Meshes.card2)
    Groups.selector.add(Meshes.card3)

    Groups.snowyText.add(Meshes.meshS)
    Groups.snowyText.add(Meshes.meshN)
    Groups.snowyText.add(Meshes.meshO)
    Groups.snowyText.add(Meshes.meshW)
    Groups.snowyText.add(Meshes.meshY)
  }

  def setup(): Unit = {
    positions()
    addGroups()

    addItems()
  }
  def addItems(): Unit = {
    scene.add(amb)
    scene.add(light)
    scene.add(Groups.selector)
    scene.add(Groups.snowyText)
    scene.add(Meshes.input)
    scene.add(Groups.arrow)

    renderer.render(scene, camera)
  }

  def removeAll(): Unit = {
    scene.remove(amb)
    scene.remove(light)
    scene.remove(Groups.selector)
    scene.remove(Groups.snowyText)
    scene.remove(Meshes.input)
    scene.remove(Groups.arrow)
  }
  private var connected: Option[Connection] = None

  private var skiColor: SkiColor = BasicSkis
  private var sledKind: SledKind = BasicSled

  val chosenSled = document.getElementById("chosen").asInstanceOf[html.Div]

  var raycaster = new THREE.Raycaster()
  var mouse     = new THREE.Vector2()
  var hover     = 0
  window.addEventListener(
    "mousemove", { e: MouseEvent =>
      mouse.x = (e.clientX / window.innerWidth) * 2 - 1
      mouse.y = (e.clientY / window.innerHeight) * -2 + 1

      raycaster.setFromCamera(mouse, camera)

      val intersects = raycaster.intersectObjects(
        js.Array(
          Groups.tree.children(1),
          Groups.tree.children(2),
          Groups.tree2.children(1),
          Groups.tree2.children(2)
        )
      );

      val leaf1 = Groups.tree.children(1).asInstanceOf[THREE.Mesh]
      val leaf2 = Groups.tree.children(2).asInstanceOf[THREE.Mesh]

      val leaf3 = Groups.tree2.children(1).asInstanceOf[THREE.Mesh]
      val leaf4 = Groups.tree2.children(2).asInstanceOf[THREE.Mesh]
      leaf1.material.asInstanceOf[THREE.MeshPhongMaterial].emissive.setHex(0x0)
      leaf2.material.asInstanceOf[THREE.MeshPhongMaterial].emissive.setHex(0x0)
      Groups.tree.children(1).name = "right"
      Groups.tree.children(2).name = "right"
      leaf3.material.asInstanceOf[THREE.MeshPhongMaterial].emissive.setHex(0x0)
      leaf4.material.asInstanceOf[THREE.MeshPhongMaterial].emissive.setHex(0x0)
      Groups.tree2.children(1).name = "left"
      Groups.tree2.children(2).name = "left"

      if (intersects.length > 0) {
        val mat = leaf1.material.clone().asInstanceOf[THREE.MeshPhongMaterial]
        mat.emissive.setHex(0x222222)
        mat.shading = THREE.FlatShading
        val mat2 = leaf2.material.clone().asInstanceOf[THREE.MeshPhongMaterial]
        mat2.emissive.setHex(0x222222)
        mat2.shading = THREE.FlatShading

        if (intersects(0).`object`.name == "right") {
          Groups.tree.children(1).asInstanceOf[THREE.Mesh].material = mat
          Groups.tree.children(2).asInstanceOf[THREE.Mesh].material = mat2
          hover = -1

        } else {
          Groups.tree2.children(1).asInstanceOf[THREE.Mesh].material = mat
          Groups.tree2.children(2).asInstanceOf[THREE.Mesh].material = mat2
          hover = 1

        }
      } else {
        hover = 0
      }

      renderer.render(scene, camera)
    },
    false
  )
  document.addEventListener(
    "mousedown", { _: Event =>
      val currentIndex = SledKinds.allSleds.indexOf(sledKind)

      hover match {
        case -1 =>
          sledKind =
            if (currentIndex > 0) SledKinds.allSleds(currentIndex - 1)
            else SledKinds.allSleds.last
          chosenSled.innerHTML = "Sled: " + sledKind.toString.replace("Sled", "")
        case 1 =>
          sledKind =
            if (currentIndex < SledKinds.allSleds.length - 1)
              SledKinds.allSleds(currentIndex + 1)
            else SledKinds.allSleds.head
          chosenSled.innerHTML = "Sled: " + sledKind.toString.replace("Sled", "")
        case _ =>
      }
    }
  )

  def switch(game: Boolean) {
    if (game) {
      document
        .getElementById("game-div")
        .asInstanceOf[html.Div]
        .classList
        .remove("back")
      document
        .getElementById("login-form")
        .asInstanceOf[html.Div]
        .classList
        .add("hide")
    } else {
      document.getElementById("game-div").asInstanceOf[html.Div].classList.add("back")
      document
        .getElementById("login-form")
        .asInstanceOf[html.Div]
        .classList
        .remove("hide")
    }
  }

  val textInput = document.getElementById("username").asInstanceOf[html.Input]
  val gameHud   = document.getElementById("game-hud").asInstanceOf[html.Div]
  document
    .getElementById("login-form")
    .asInstanceOf[html.Form]
    .addEventListener(
      "submit", { e: Event =>
        e.preventDefault()
        //Connect to the WebSocket server
        connected match {
          case x if x.isEmpty =>
            connected = Some(
              new Connection(
                document.getElementById("username").asInstanceOf[html.Input].value,
                sledKind,
                skiColor
              )
            )
          case x if x.isDefined => connected.get.reSpawn()
          case _                =>
        }

        switch(true)
        LoginScreen2.removeAll()
        gameHud.classList.remove("hide")
        DrawState2.setup()
        GameState.startRedraw()
        renderer.render(scene, camera)
      },
      false
    )

  def rejoinPanel() {
    switch(false)

    GameState.stopRedraw()
    DrawState2.removeAll()
    gameHud.classList.add("hide")
    textInput.focus()
    camera.position.set(0, 100, -100)
    camera.lookAt(new THREE.Vector3(0, 0, 0))
    LoginScreen2.addItems()
  }
}
